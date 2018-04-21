/*
 * Copyright (c) 2018 Fondesa
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

package com.fondesa.database.strategy

import com.fondesa.database.Database
import com.fondesa.database.statement.CreateTable
import com.fondesa.database.statement.Delete
import com.fondesa.database.statement.Pragma
import com.fondesa.database.statement.Vacuum
import com.fondesa.database.structure.Graph
import javax.inject.Inject

/**
 * Default implementation of [UpgradeStrategy] that will delete all
 * tables, foreign keys, indexes and triggers on version upgrade.
 * To avoid the check on the tables' relationships, the delete of all tables is done
 * through the writing on the <i>sqlite_master</i> table.
 * The writing on the <i>sqlite_master</i> table is discouraged so, in production mode,
 * it's better to specify a custom [UpgradeStrategy].
 */
class DefaultUpgradeStrategy @Inject constructor() : UpgradeStrategy {

    override fun onUpgrade(database: Database, oldVersion: Int, newVersion: Int, newGraph: Graph) {
        val pragmaWritableSchemaRaw = "writable_schema"

        // Enable the writing on sqlite_master.
        val enableWritableSchema = Pragma.write(pragmaWritableSchemaRaw, true)
        database.compile(enableWritableSchema).execute(close = true)

        val dropAll = Delete.from("sqlite_master")
            .where("type in ('table', 'index', 'trigger')")
            .build()

        // Delete all tables, foreign keys, indexes and triggers.
        database.compile(dropAll).execute(close = true)

        // Disable the writing on sqlite_master.
        val disableWritableSchema = Pragma.write(pragmaWritableSchemaRaw, false)
        database.compile(disableWritableSchema).execute(close = true)
    }

    override fun onPostUpgrade(
        database: Database,
        oldVersion: Int,
        newVersion: Int,
        newGraph: Graph
    ) {
        // Check the integrity of the database file.
        val integrityCheck = database.compile(Pragma.readString("integrity_check")).execute()
        if (integrityCheck != "ok") {
            val vacuum = Vacuum.create()
            // Attempt to rebuild the database file.
            database.compile(vacuum).execute()
        }

        database.transaction {
            // Creates tables contained in the graph.
            newGraph.getTables().forEach {
                database.compile(CreateTable.of(it)).execute(close = true)
            }
        }
    }
}