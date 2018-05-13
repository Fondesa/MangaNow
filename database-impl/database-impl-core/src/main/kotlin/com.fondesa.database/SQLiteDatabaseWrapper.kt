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

package com.fondesa.database

import android.database.sqlite.SQLiteDatabase
import com.fondesa.common.database.Database
import com.fondesa.common.database.execution.Closeable
import com.fondesa.common.database.execution.Executor
import com.fondesa.common.database.statement.Statement
import com.fondesa.database.statement.base.SQLiteDatabaseReceiver

/**
 * Used to define the behavior of a SQLite database.
 *
 * @param sqLiteDatabase instance of [SQLiteDatabase].
 */
class SQLiteDatabaseWrapper(private val sqLiteDatabase: SQLiteDatabase) : Database {

    override fun transaction(tx: () -> Unit) {
        sqLiteDatabase.beginTransaction()
        try {
            tx()
            // If no exceptions were found, set the transaction as successful.
            sqLiteDatabase.setTransactionSuccessful()
        } finally {
            // Commit the transaction.
            sqLiteDatabase.endTransaction()
        }
    }

    override fun <E : Executor<*>> compile(statement: Statement<E>): E {
        // Attach the SQLiteDatabase instance to the statement.
        (statement as? SQLiteDatabaseReceiver)?.injectDatabase(sqLiteDatabase)
        // create the executor.
        return statement.createExecutor()
    }

    override fun finalize(vararg closeables: Closeable) {
        // Close all the Closeable instances.
        closeables.forEach { it.close() }
    }
}