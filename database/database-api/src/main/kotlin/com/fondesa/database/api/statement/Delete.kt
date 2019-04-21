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

import com.fondesa.database.api.clause.Expression
import com.fondesa.database.api.clause.RawExpression
import com.fondesa.database.api.execution.PlainExecutor
import com.fondesa.database.api.statement.base.SQLiteCompiledStatement

/**
 * Helper class used to create a DELETE statement of SQLite.
 * The DELETE statement is used to delete records from a table.
 */
object Delete {

    /**
     * Creates a [Builder] used to delete records from a table.
     *
     * @param table table with records that must be deleted.
     * @return instance of [Builder].
     */
    fun from(table: String) = Builder().from(table)

    /**
     * Creates a raw DELETE [Statement].
     *
     * @param statement raw string used as statement.
     * @return [Statement] with a [PlainExecutor] that returns the number of deleted rows.
     */
    fun raw(statement: String, vararg args: String) = SQLiteCompiledStatement.lined(statement) {
        PlainExecutor(it) {
            // Bind all the arguments skipping the columns.
            it.bindAllArgsAsStrings(args)
            // Get the number of deleted rows.
            val recordCount = it.executeUpdateDelete()
            // Clear the bindings after each delete.
            it.clearBindings()
            recordCount
        }
    }

    /**
     * Builder used to delete records from a table with a DELETE statement.
     */
    class Builder internal constructor() {
        private lateinit var table: String
        private var where: Expression? = null

        /**
         * Sets the WHERE clause to define additional conditions to delete records.
         * If not specified, all records in the table will be deleted.
         *
         * @param expression instance of [Expression] that defines the WHERE clause.
         */
        fun where(expression: Expression) = apply { where = expression }

        /**
         * Sets the WHERE clause to define additional conditions to delete records.
         * If not specified, all records in the table will be deleted.
         *
         * @param raw raw WHERE clause without the WHERE keyword.
         * @param args the arguments that will be bound into the compiled statement.
         */
        fun where(raw: String, vararg args: String) = where(
            RawExpression(
                raw,
                *args
            )
        )

        /**
         * Sets the table from which the records will be deleted.
         *
         * @param table table's name.
         */
        internal fun from(table: String) = apply { this.table = table }

        /**
         * Creates the raw statement managed by [Delete.raw].
         */
        fun build(): SQLiteCompiledStatement<PlainExecutor<Int>> {
            val sql = StringBuilder("DELETE FROM ")
                .append(table)

            where?.let {
                sql.append(" WHERE(")
                    .append(it.raw())
                    .append(')')
            }

            val args = where?.args() ?: emptyArray()
            return raw(sql.toString(), *args)
        }
    }
}