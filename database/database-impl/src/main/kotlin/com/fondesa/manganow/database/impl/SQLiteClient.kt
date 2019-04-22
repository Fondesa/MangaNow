/*
 * Copyright (c) 2019 Fondesa
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fondesa.manganow.database.impl

import android.content.Context
import android.database.DatabaseErrorHandler
import android.database.sqlite.SQLiteException
import com.fondesa.manganow.database.api.client.Database
import com.fondesa.manganow.database.api.client.DatabaseClient
import com.fondesa.manganow.database.api.client.statement.CreateTable
import com.fondesa.manganow.database.api.client.statement.Pragma
import com.fondesa.manganow.database.api.structure.Graph
import com.fondesa.manganow.database.impl.qualifiers.SQLiteDatabaseInfo
import com.fondesa.manganow.database.impl.strategy.ErrorStrategy
import com.fondesa.manganow.database.impl.strategy.UpgradeStrategy
import com.fondesa.manganow.log.api.Log
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Used to manage the creation and the access to a SQLite database.
 * The creation of the database is done on a background thread till it isn't accessed
 * for the first time.
 * If the database is accessed during its creation, the creation will block the main thread.
 *
 * @param context [Context] used to create the database.
 * @param name name of the database.
 * @param version database version.
 * @param graph tables' graph used to define the database's structure.
 * @param upgradeStrategy strategy used to migrate the database on version upgrade.
 * @param errorStrategy strategy used to launch actions when database is corrupted.
 */
open class SQLiteClient @Inject constructor(
    private val context: Context,
    @SQLiteDatabaseInfo private val name: String,
    @SQLiteDatabaseInfo private val version: Int,
    private val graph: Graph,
    private val upgradeStrategy: UpgradeStrategy,
    private val errorStrategy: ErrorStrategy
) : DatabaseClient {

    private var isInitialized = false
    private val errorHandler = DatabaseErrorHandler {
        errorStrategy.onCorruption(getDatabase())
    }

    private lateinit var database: Database
    private val lock = Object()

    override fun createDatabase() {
        GlobalScope.launch {
            // Dispatch the creation on the background thread.
            synchronized(lock) {
                val context = this@SQLiteClient.context
                Log.d("Started creation or opening of the database.")
                // Open or create the database.
                val sqlDatabase = context.openOrCreateDatabase(name, 0, null, errorHandler)
                    ?: throw NullPointerException("The creation or opening of the database wasn't successful.")

                // Create the database wrapper.
                database = SQLiteDatabaseWrapper(sqlDatabase)
                onConfigure(database)

                val pragmaSchemaVersionRaw = "user_version"
                val readVersionPragma = Pragma.readLong(pragmaSchemaVersionRaw)
                // Read the schema version.
                val version = database.compile(readVersionPragma).execute(close = true).toInt()
                val newVersion = this@SQLiteClient.version
                if (version != newVersion) {
                    // If the versions are different, the database must be created or upgraded.
                    var created = false
                    var upgraded = false
                    database.transaction {
                        when {
                            version == 0 -> {
                                // Create the database for the first time.
                                onCreate(database)
                                created = true
                            }

                            version > newVersion -> throw SQLiteException("Can't downgrade database from version $version to $newVersion")

                            else -> {
                                // Migrate the database to the new schema.
                                onUpgrade(database, version, newVersion)
                                upgraded = true
                            }
                        }
                        val increaseVersionPragma =
                            Pragma.write(pragmaSchemaVersionRaw, newVersion)
                        // Increase the database version.
                        database.compile(increaseVersionPragma).execute(close = true)
                    }
                    if (created) {
                        onPostCreate(database)
                    } else if (upgraded) {
                        onPostUpgrade(database, version, newVersion)
                    }
                }

                // Open the database.
                onOpen(database)
                isInitialized = true
                Log.d("End creation or opening of the database.")

                // Unlock the lock to retrieve the database.
                lock.notifyAll()
            }
        }
    }

    override fun getDatabase(): Database {
        synchronized(lock) {
            while (!isInitialized) {
                Log.d("Waiting the database initialization...")
                // Lock the retrieving of the database on the main thread
                // till the creation or opening of the database isn't finished.
                lock.wait()
            }
        }
        return database
    }

    /**
     * Specify the general database's configurations for this session.
     * This method is called only one time during this session.
     *
     * @param database current instance of [Database].
     */
    open fun onConfigure(database: Database) {
        // By default, foreign keys are disabled. They must be enabled if they are used.
        database.compile(Pragma.write("foreign_keys", true)).execute(close = true)
    }

    /**
     * Creates the schema with its' tables, indexes, etc..
     * The tables are retrieved from the [Graph] passed in the constructor.
     * This method is called only the first time the database has to be created.
     * This method runs in a database transaction.
     *
     * @param database current instance of [Database].
     */
    open fun onCreate(database: Database) {
        // Creates tables contained in the graph.
        graph.getTables().forEach {
            database.compile(CreateTable.of(it)).execute(close = true)
        }
    }

    /**
     * Migrate the schema when the version of the database is increased.
     * This method is called only once during the session when the version of the database increases.
     * This method runs in a database transaction.
     *
     * @param database current instance of [Database].
     * @param oldVersion previous version of the schema.
     * @param newVersion new version of the schema.
     */
    open fun onUpgrade(database: Database, oldVersion: Int, newVersion: Int) {
        // Delegates the upgrade to the MigrationStrategy.
        upgradeStrategy.onUpgrade(database, oldVersion, newVersion, graph)
    }

    /**
     * Called after [onCreate] outside of a database transaction.
     * If [onCreate] wasn't called, this method won't be called.
     *
     * @param database current instance of [Database].
     */
    open fun onPostCreate(database: Database) {
        // Empty by default.
    }

    /**
     * Called after [onUpgrade] outside of a database transaction.
     * If [onUpgrade] wasn't called, this method won't be called.
     *
     * @param database current instance of [Database].
     * @param oldVersion previous version of the schema.
     * @param newVersion new version of the schema.
     */
    private fun onPostUpgrade(database: Database, oldVersion: Int, newVersion: Int) {
        // Delegates the upgrade to the MigrationStrategy.
        upgradeStrategy.onPostUpgrade(database, oldVersion, newVersion, graph)
    }

    /**
     * Specify an additional database's behavior when it's opened.
     * This method is called only one time during this session.
     *
     * @param database current instance of [Database].
     */
    open fun onOpen(database: Database) {
        // Empty by default.
    }
}