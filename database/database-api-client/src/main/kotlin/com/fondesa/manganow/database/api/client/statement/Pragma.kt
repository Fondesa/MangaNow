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

package com.fondesa.manganow.database.api.client.statement

import android.database.sqlite.SQLiteStatement
import com.fondesa.manganow.database.api.client.execution.PlainExecutor
import com.fondesa.manganow.database.api.client.statement.base.SQLiteCompiledStatement

/**
 * Helper class used to create a PRAGMA statement of SQLite.
 * The PRAGMA statement is used to to modify the operation of the SQLite library or
 * to query the SQLite library for internal (non-table) data.
 */
object Pragma {

    /**
     * Creates a raw PRAGMA [Statement] used to execute a query on the internal (non-table) data
     * of the database that will return the first value as long.
     *
     * @param name name of the PRAGMA.
     * @return [Statement] with a [PlainExecutor] that will run a query.
     */
    fun readLong(name: String) =
        raw("PRAGMA $name") { it.simpleQueryForLong() }

    /**
     * Creates a raw PRAGMA [Statement] used to execute a query on the internal (non-table) data
     * of the database that will return the first value as string.
     *
     * @param name name of the PRAGMA.
     * @return [Statement] with a [PlainExecutor] that will run a query.
     */
    fun readString(name: String) =
        raw("PRAGMA $name") { it.simpleQueryForString() }

    /**
     * Creates a raw PRAGMA [Statement] used to set a value of an internal property of the database.
     *
     * @param name name of the property that will be set.
     * @param value value of the property that will be set
     * @return [Statement] with a [PlainExecutor] that will be executed without returning any result.
     */
    fun write(name: String, value: Any) =
        raw("PRAGMA $name = $value") { it.execute() }

    /**
     * Creates a raw PRAGMA [Statement].
     *
     * @param statement raw string used as statement.
     * @param execution block that will be executed when the [Statement] will be executed.
     * @return [Statement] with a [PlainExecutor] that will execute [execution].
     */
    fun <T> raw(statement: String, execution: (SQLiteStatement) -> T) =
        SQLiteCompiledStatement.lined(statement) {
            PlainExecutor(it) {
                execution(it)
            }
        }
}