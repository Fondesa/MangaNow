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

import com.fondesa.manganow.database.api.clause.ConflictType
import com.fondesa.manganow.database.api.execution.BinderExecutor
import com.fondesa.manganow.database.api.execution.IndexedBinderExecutor
import com.fondesa.manganow.database.api.extension.isNullOrEmpty
import com.fondesa.manganow.database.api.extension.plusArray
import com.fondesa.manganow.database.api.statement.base.SQLiteCompiledStatement
import com.fondesa.manganow.database.api.structure.Column

/**
 * Helper class used to create an INSERT statement of SQLite.
 * The INSERT statement is used to insert records into a table.
 */
object Insert {

    /**
     * Creates a [Builder] used to insert records into a table.
     *
     * @param table table in which records will be inserted.
     * @return instance of [Builder].
     */
    fun into(table: String) = Builder().into(table)

    /**
     * Creates a raw INSERT [Statement].
     *
     * @param statement raw string used as statement.
     * @return [Statement] with a [BinderExecutor] that returns the id of the row that was inserted
     * if the INSERT was successful or -1 if the INSERT failed for some reason.
     */
    fun raw(statement: String) = SQLiteCompiledStatement.lined(statement) {
        BinderExecutor(it) {
            // Get the last inserted row id.
            val rowId = it.executeInsert()
            // Clear the bindings after each insert.
            it.clearBindings()
            rowId
        }
    }

    /**
     * Builder used to insert records into a table with an INSERT statement.
     */
    class Builder internal constructor() {
        private lateinit var table: String
        private var columns: Array<Column<*>>? = null
        private var conflictType: ConflictType? = null

        /**
         * Sets the [Column]s of the record that must be inserted.
         * At least one column is required.
         *
         * @param columns [Column]s used in the INSERT statement.
         */
        fun columns(columns: Array<Column<*>>) = apply { this.columns = columns }

        /**
         * Sets the [Column]s of the record that must be inserted.
         * The [Column] parameters are repeated to have an array with at least 1 [Column].
         *
         * @param first first [Column].
         * @param others other [Column]s that will be concatenated to the first one.
         */
        fun columns(first: Column<*>, vararg others: Column<*>) =
            apply { columns = first.plusArray(others) }

        /**
         * Sets the conflict type of the INSERT statement.
         * The conflict resolution algorithm applies when a conflict after the INSERT execution occurs.
         * The default algorithm is [ConflictType.ABORT].
         *
         * @param conflictType one of the conflict types defined in [ConflictType.Type].
         */
        fun conflictType(conflictType: ConflictType) = apply { this.conflictType = conflictType }

        /**
         * Sets the table's name of the table in which the records will be inserted.
         *
         * @param table table's name.
         */
        internal fun into(table: String) = apply { this.table = table }

        /**
         * Creates a raw INSERT [Statement].
         *
         * @return [Statement] with a [IndexedBinderExecutor] that returns the id of the row that was inserted
         * if the INSERT was successful or -1 if the INSERT failed for some reason.
         */
        fun build(): SQLiteCompiledStatement<IndexedBinderExecutor<Long>> {
            val columns = if (columns.isNullOrEmpty()) {
                throw IllegalArgumentException("You have to specify at least one column.")
            } else columns!!

            val sql = StringBuilder("INSERT")
            conflictType?.let {
                // Add the conflict type.
                sql.append(" OR ")
                    .append(conflictType)
            }
            sql.append(" INTO ")
                .append(table)
                .append('(')

            val valuesSb = StringBuilder()
            columns.forEachIndexed { index, col ->
                sql.append(col.name)
                // Add the column's placeholder.
                valuesSb.append('?')
                if (index < columns.lastIndex) {
                    sql.append(',')
                    valuesSb.append(',')
                }
            }

            sql.append(")VALUES(")
                .append(valuesSb.toString())
                .append(')')

            return SQLiteCompiledStatement.lined(sql.toString()) {
                IndexedBinderExecutor(it, columns) {
                    val rowId = it.executeInsert()
                    it.clearBindings()
                    rowId
                }
            }
        }
    }
}