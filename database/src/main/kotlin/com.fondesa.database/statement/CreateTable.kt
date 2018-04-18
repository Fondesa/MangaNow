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

import android.os.Build
import com.fondesa.database.execution.PlainExecutor
import com.fondesa.database.extension.interpolateWith
import com.fondesa.database.extension.isNullOrEmpty
import com.fondesa.database.extension.plusArray
import com.fondesa.database.statement.base.SQLiteCompiledStatement
import com.fondesa.database.structure.Column
import com.fondesa.database.structure.ForeignKey
import com.fondesa.database.structure.Table

/**
 * Helper class used to create a CREATE TABLE statement of SQLite.
 * The CREATE TABLE statement is used to create a table in a SQLite database.
 */
object CreateTable {

    /**
     * Creates a [Builder] used to create a table.
     *
     * @param table table that must be created.
     * @return instance of [Builder].
     */
    fun of(table: String) = Builder().table(table)

    /**
     * Creates raw CREATE TABLE [Statement] using the properties of [table].
     *
     * @param table instance of [Table] that defines table's properties.
     * @param notExists true if the table must be created only if it doesn't exist.
     * If [notExists] is passed as false and the table exists, the execution will result in an exception.
     * @return [Statement] with a [PlainExecutor].
     */
    fun of(table: Table, notExists: Boolean = true): SQLiteCompiledStatement<PlainExecutor<Unit>> {
        val builder = of(table.getName())
            .columns(table.getColumns())
            .foreignKeys(*table.getForeignKeys())

        if (!table.withRowId())
            builder.withoutRowId()

        if (notExists)
            builder.ifNotExists()

        return builder.build()
    }

    /**
     * Creates a raw CREATE TABLE [Statement].
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
     * Builder used to create a table with a CREATE TABLE statement.
     */
    class Builder internal constructor() {
        private lateinit var table: String
        private var columns: Array<Column<*>>? = null
        private var foreignKeys: Array<out ForeignKey>? = null
        private var ifNotExists = false
        private var withRowId = true

        /**
         * Sets the [Column]s that must be used created inside the table.
         * At least one column is required.
         *
         * @param columns [Column]s used in the table.
         */
        fun columns(columns: Array<Column<*>>) = apply { this.columns = columns }

        /**
         * Sets the [Column]s that must be used created inside the table.
         * The [Column] parameters are repeated to have an array with at least 1 [Column].
         *
         * @param first first [Column].
         * @param others other [Column]s that will be concatenated to the first one.
         */
        fun columns(first: Column<*>, vararg others: Column<*>) =
            apply { columns = first.plusArray(others) }

        /**
         * Sets the foreign keys used to relate [Column]s of different tables.
         *
         * @param foreignKeys array of foreign keys.
         */
        fun foreignKeys(vararg foreignKeys: ForeignKey) = apply { this.foreignKeys = foreignKeys }

        /**
         * Sets the clause IF NOT EXISTS on the [Statement].
         * If used, when a table exists, the new table won't be created.
         * If not used, when a table exists, the execution of the [Statement] will result in an exception.
         */
        fun ifNotExists() = apply { ifNotExists = true }

        /**
         * Sets the clause WITHOUT ROWID on the [Statement].
         * The rowid is an inner autoincrement column added in all SQLite database's tables by default.
         * From api 21 it's possible to avoid the creation of this column as a performance optimization.
         * Remember that in a table, at least one primary key column is necessary so,
         * if this method is invoked, another column in the same table must be a primary key column.
         */
        fun withoutRowId() = apply { withRowId = false }

        /**
         * Sets the table's name of the table that must be created.
         *
         * @param table table's name.
         */
        internal fun table(table: String) = apply { this.table = table }

        /**
         * Creates the raw statement managed by [CreateTable.raw].
         */
        fun build(): SQLiteCompiledStatement<PlainExecutor<Unit>> {
            val columns = if (columns.isNullOrEmpty()) {
                throw IllegalArgumentException("You have to specify at least one column.")
            } else columns!!

            val sql = StringBuilder("CREATE TABLE ")

            if (ifNotExists) {
                sql.append("IF NOT EXISTS ")
            }
            sql.append(table)
                .append('(')

            val primaryKeys = mutableListOf<String>()
            val uniqueKeys = mutableListOf<String>()

            columns.forEachIndexed { index, column ->
                val name = column.name
                sql.append(name)
                sql.append(' ')
                sql.append(column.type)
                if (column.isNotNull) {
                    // Specify that column mustn't be null.
                    sql.append(" NOT NULL")
                }
                column.defaultValue?.let { value ->
                    // Add default column value.
                    sql.append(" DEFAULT ")
                        .append(value.toString())
                }
                if (index < columns.lastIndex) {
                    sql.append(',')
                }

                if (column.isPrimaryKey) {
                    primaryKeys.add(name)
                }

                if (column.isUnique) {
                    uniqueKeys.add(name)
                }
            }
            if (primaryKeys.isNotEmpty()) {
                sql.append(",PRIMARY KEY(")
                    .append(primaryKeys.interpolateWith(','))
                    .append(')')
            }

            if (uniqueKeys.isNotEmpty()) {
                sql.append(",UNIQUE(")
                    .append(uniqueKeys.interpolateWith(','))
                    .append(')')
            }

            if (!foreignKeys.isNullOrEmpty()) {
                // Manage foreign keys.
                val foreignKeys = foreignKeys!!
                sql.append(',')
                foreignKeys.forEachIndexed { index, key ->
                    val toColumns = key.toColumns
                    sql.append("FOREIGN KEY(")
                        .append(key.fromColumns.interpolateWith(',') { it.name })
                        .append(") REFERENCES ")
                        .append(toColumns.first().tableName)
                        .append('(')
                        .append(toColumns.interpolateWith(',') { it.name })
                        .append(')')

                    key.conflictStrategies.forEach { (clause, action) ->
                        // Add clause and action on foreign key.
                        sql.append(' ')
                            .append(clause.value)
                            .append(' ')
                            .append(action.value)
                    }

                    if (index < foreignKeys.lastIndex) {
                        sql.append(',')
                    }
                }
            }

            sql.append(')')

            if (!withRowId && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                // The clause "WITHOUT ROWID" is available only from api 21.
                sql.append(" WITHOUT " + Column.ROW_ID_NAME)
            }

            return raw(sql.toString())
        }
    }
}