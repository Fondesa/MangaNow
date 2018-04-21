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

package com.fondesa.database.statement

import com.fondesa.database.clause.ConflictType
import com.fondesa.database.clause.Expression
import com.fondesa.database.clause.RawExpression
import com.fondesa.database.execution.BinderExecutor
import com.fondesa.database.execution.IndexedBinderExecutor
import com.fondesa.database.extension.bindAllArgsAsStrings
import com.fondesa.database.extension.isNullOrEmpty
import com.fondesa.database.extension.plusArray
import com.fondesa.database.statement.base.SQLiteCompiledStatement
import com.fondesa.database.structure.Column

/**
 * Helper class used to create an UPDATE statement of SQLite.
 * The UPDATE statement is used to update records into a table.
 */
object Update {

    /**
     * Creates a [Builder] used to update records into a table.
     *
     * @param table table in which records will be updated.
     * @return instance of [Builder].
     */
    fun table(table: String) = Builder().table(table)

    /**
     * Creates a raw UPDATE [Statement].
     *
     * @param statement raw string used as statement.
     * @return [Statement] with a [BinderExecutor] that returns the number of updated rows.
     */
    fun raw(statement: String, argsGap: Int = 0, vararg args: String) =
        SQLiteCompiledStatement.lined(statement) {
            BinderExecutor(it) {
                // Bind all the arguments skipping the columns.
                it.bindAllArgsAsStrings(args, argsGap)
                // Get the number of updated rows.
                val recordCount = it.executeUpdateDelete()
                // Clear the bindings after each update.
                it.clearBindings()
                recordCount
            }
        }

    /**
     * Builder used to update records into a table with an UPDATE statement.
     */
    class Builder internal constructor() {
        private lateinit var table: String
        private var columns: Array<Column<*>>? = null
        private var conflictType: ConflictType? = null
        private var limit: String? = null
        private var orderBy: String? = null
        private var where: Expression? = null

        /**
         * Sets the [Column]s of the record that must be updated.
         * At least one column is required.
         *
         * @param columns [Column]s used in the UPDATE statement.
         */
        fun columns(columns: Array<Column<*>>) = apply { this.columns = columns }

        /**
         * Sets the [Column]s of the record that must be updated.
         * The [Column] parameters are repeated to have an array with at least 1 [Column].
         *
         * @param first first [Column].
         * @param others other [Column]s that will be concatenated to the first one.
         */
        fun columns(first: Column<*>, vararg others: Column<*>) =
            apply { columns = first.plusArray(others) }

        /**
         * Sets the conflict type of the UPDATE statement.
         * The conflict resolution algorithm applies when a conflict after the UPDATE execution occurs.
         * The default algorithm is [ConflictType.ABORT].
         *
         * @param conflictType one of the conflict types defined in [ConflictType.Type].
         */
        fun conflictType(conflictType: ConflictType) = apply { this.conflictType = conflictType }

        /**
         * Sets the WHERE clause to define additional conditions to update records.
         * If not specified, all records in the table will be update.
         *
         * @param expression instance of [Expression] that defines the WHERE clause.
         */
        fun where(expression: Expression) = apply { where = expression }

        /**
         * Sets the WHERE clause to define additional conditions to update records.
         * If not specified, all records in the table will be updated.
         *
         * @param raw raw WHERE clause without the WHERE keyword.
         * @param args the arguments that will be bound into the compiled statement.
         */
        fun where(raw: String, vararg args: String) = where(RawExpression(raw, *args))

        /**
         * Sets the table in which the records will be updated.
         *
         * @param table table's name.
         */
        internal fun table(table: String) = apply { this.table = table }

        /**
         * Creates the raw statement managed by [Update.raw].
         */
        fun build(): SQLiteCompiledStatement<IndexedBinderExecutor<Int>> {
            val columns = if (columns.isNullOrEmpty()) {
                throw IllegalArgumentException("You have to specify at least one column.")
            } else columns!!

            val sql = StringBuilder("UPDATE")
            conflictType?.let {
                sql.append(" OR ")
                    .append(it)
            }
            sql.append(' ')
                .append(table)
                .append(" SET ")

            columns.forEachIndexed { index, column ->
                sql.append(column.name)
                    .append("=?")
                if (index < columns.lastIndex) {
                    sql.append(',')
                }
            }

            where?.let {
                sql.append(" WHERE(")
                    .append(it.raw())
                    .append(')')
            }
            orderBy?.let { sql.append(it) }
            limit?.let { sql.append(it) }

            val args = where?.args() ?: emptyArray()
            val columnCount = columns.size
            return SQLiteCompiledStatement.lined(sql.toString()) {
                IndexedBinderExecutor(it, columns) {
                    // Bind all the arguments skipping the columns.
                    it.bindAllArgsAsStrings(args, columnCount)
                    // Get the number of updated rows.
                    val recordCount = it.executeUpdateDelete()
                    // Clear the bindings after each update.
                    it.clearBindings()
                    recordCount
                }
            }
        }
    }
}