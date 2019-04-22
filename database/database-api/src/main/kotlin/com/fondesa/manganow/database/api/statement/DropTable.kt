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

package com.fondesa.manganow.database.api.statement

import com.fondesa.manganow.database.api.execution.PlainExecutor
import com.fondesa.manganow.database.api.statement.base.SQLiteCompiledStatement
import com.fondesa.manganow.database.api.structure.Table

/**
 * Helper class used to create a DROP TABLE statement of SQLite.
 * The DROP TABLE statement is used to delete a table from a SQLite database.
 */
object DropTable {

    /**
     * Creates raw DROP TABLE [Statement] using the properties of [table].
     *
     * @param table table's name of the table that must be deleted.
     * @param exists true if the table must be deleted only if it exists.
     * If [exists] is passed as false and the table doesn't exist, the execution will result in an exception.
     * @return [Statement] with a [PlainExecutor].
     */
    fun of(table: String, exists: Boolean = true): SQLiteCompiledStatement<PlainExecutor<Unit>> {
        val builder = Builder().table(table)

        if (exists) {
            builder.ifExists()
        }
        return builder.build()
    }

    /**
     * Creates raw DROP TABLE [Statement] using the properties of [table].
     *
     * @param table instance of [Table] that defines table's properties.
     * @param exists true if the table must be deleted only if it exists.
     * If [exists] is passed as false and the table doesn't exist, the execution will result in an exception.
     * @return [Statement] with a [PlainExecutor].
     */
    fun of(table: Table, exists: Boolean = true) =
        of(table.getName(), exists)

    /**
     * Creates a raw DROP TABLE [Statement].
     *
     * @param statement raw string used as statement.
     * @return [Statement] with a [PlainExecutor].
     */
    fun raw(statement: String) = SQLiteCompiledStatement.lined(statement) {
        PlainExecutor(it) {
            it.execute()
        }
    }

    /**
     * Builder used to delete a table with a DROP TABLE statement.
     */
    class Builder internal constructor() {
        private lateinit var table: String
        private var ifExists = false

        /**
         * Sets the clause IF EXISTS on the [Statement].
         * If used, when a table doesn't exist, the execution won't do anything.
         * If not used, when a table doesn't exist, the execution of the [Statement] will result in an exception.
         */
        fun ifExists() = apply { ifExists = true }

        /**
         * Sets the table's name of the table that must be deleted.
         *
         * @param table table's name.
         */
        internal fun table(table: String) = apply { this.table = table }

        /**
         * Creates the raw statement managed by [DropTable.raw].
         */
        fun build(): SQLiteCompiledStatement<PlainExecutor<Unit>> {
            val sql = StringBuilder("DROP TABLE ")
            if (ifExists) {
                sql.append("IF EXISTS ")
            }
            sql.append(table)
            return raw(sql.toString())
        }
    }
}