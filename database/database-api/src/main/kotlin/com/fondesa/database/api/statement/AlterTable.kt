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

package com.fondesa.database.api.statement

import com.fondesa.database.api.execution.PlainExecutor
import com.fondesa.database.api.statement.base.SQLiteCompiledStatement
import com.fondesa.database.api.structure.Column

/**
 * Helper class used to create an ALTER TABLE statement of SQLite.
 * The ALTER TABLE statement is used to modify a table's name or
 * to add a [Column] to an existing table.
 */
object AlterTable {

    /**
     * Creates a [RenameBuilder] used to rename a table.
     *
     * @param table table that must be renamed.
     * @return instance of [RenameBuilder].
     */
    fun rename(table: String) = RenameBuilder().table(table)

    /**
     * Creates a [AddBuilder] used to add a column to a table.
     *
     * @param column [Column] that must be added to the table
     * @return instance of [AddBuilder].
     */
    fun add(column: Column<*>) = AddBuilder().add(column)

    /**
     * Creates a raw ALTER TABLE [Statement].
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
     * Builder used to rename a table with an ALTER TABLE statement.
     */
    class RenameBuilder internal constructor() {
        private lateinit var table: String
        private var renamedTable: String? = null

        /**
         * Sets the new table's name.
         *
         * @param table new table's name.
         */
        fun to(table: String) = apply { renamedTable = table }

        /**
         * Sets the current table.
         *
         * @param table current table's name.
         */
        internal fun table(table: String) = apply { this.table = table }

        /**
         * Creates the raw statement managed by [AlterTable.raw].
         */
        fun build(): SQLiteCompiledStatement<PlainExecutor<Unit>> {
            val renamedTable = renamedTable
                ?: throw IllegalArgumentException("You have to specify the new table's name.")

            return raw("ALTER TABLE $table RENAME TO $renamedTable")
        }
    }

    /**
     * Builder used to add a column to a table with an ALTER TABLE statement.
     */
    class AddBuilder internal constructor() {
        private lateinit var addedColumn: Column<*>
        private var table: String? = null

        /**
         * Sets the table in which the column must be added.
         *
         * @param table table's name.
         */
        fun to(table: String) = apply { this.table = table }

        /**
         * Sets the [Column] that must be added to the table.
         *
         * @param column [Column] that must be added.
         */
        internal fun add(column: Column<*>) = apply { this.addedColumn = column }

        /**
         * Creates the raw statement managed by [AlterTable.raw].
         */
        fun build(): SQLiteCompiledStatement<PlainExecutor<Unit>> {
            val table = table ?: throw IllegalArgumentException("You have to specify the table.")

            return raw("ALTER TABLE $table ADD COLUMN $addedColumn")
        }
    }
}