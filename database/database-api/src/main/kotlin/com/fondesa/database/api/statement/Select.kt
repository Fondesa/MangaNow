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

import com.fondesa.database.api.clause.*
import com.fondesa.database.api.execution.QueryExecutor
import com.fondesa.database.api.extension.isNullOrEmpty
import com.fondesa.database.api.extension.plusArray
import com.fondesa.database.api.reader.SQLiteColumnReader
import com.fondesa.database.api.reader.SQLiteRawReader
import com.fondesa.database.api.statement.base.SQLiteQueryStatement
import com.fondesa.database.api.structure.Column

/**
 * Helper class used to create a SELECT statement of SQLite.
 * The SELECT statement is used to read the record of a table.
 */
object Select {

    /**
     * Creates a [Builder] used to read the records of a table.
     * The function parameters are repeated to have an array with at least 1 raw statement.
     *
     * @param first first table, raw subquery or join clause.
     * @param others other tables, raw subqueries or join clauses.
     * @return instance of [Builder].
     */
    fun from(first: String, vararg others: String): Builder {
        val full = first.plusArray(others)
        return Builder().from(full.joinToString())
    }

    /**
     * Creates a [Builder] used to read the records of a table.
     * The function parameters are repeated to have an array with at least 1 raw statement.
     *
     * @param first first subquery.
     * @param others other subqueries.
     * @return instance of [Builder].
     */
    fun from(first: SQLiteQueryStatement<*>, vararg others: SQLiteQueryStatement<*>): Builder {
        val full = first.plusArray(others)
        return Builder().from(full.joinToString { it.raw })
    }

    /**
     * Creates a raw SELECT [Statement].
     *
     * @param statement raw string used as statement.
     * @return [Statement] with a [QueryExecutor] that returns the [SQLiteRawReader] used to read the records.
     */
    fun raw(statement: String, args: Array<out String>) =
        SQLiteQueryStatement.lined(statement, args) { QueryExecutor(it) { SQLiteRawReader(it) } }

    /**
     * Builder used to read the records of a table with a SELECT statement.
     */
    class Builder internal constructor() {
        private lateinit var from: String
        private var aggregates: Array<Aggregate>? = null
        private var columns: Array<Column<*>>? = null
        private var groupByColumns: Array<out Column<*>>? = null
        private var isDistinct: Boolean = false
        private var joins = mutableListOf<String>()
        private var limit: String? = null
        private var orderBy: String? = null
        private var where: Expression? = null

        /**
         * Sets the [Aggregate] functions used in this statement.
         * At least one aggregate function is required.
         *
         * @param aggregates [Aggregate]s used in the SELECT statement.
         */
        fun aggregates(aggregates: Array<Aggregate>) = apply { this.aggregates = aggregates }

        /**
         * Sets the [Aggregate] functions used in this statement.
         * The [Aggregate] parameters are repeated to have an array with at least 1 [Aggregate].
         *
         * @param first first [Aggregate].
         * @param others other [Aggregate]s that will be concatenated to the first one.
         */
        fun aggregates(first: Aggregate, vararg others: Aggregate) =
            apply { aggregates = first.plusArray(others) }

        /**
         * Sets the [Column]s of the record that must be retrieved.
         * At least one column is required.
         *
         * @param columns [Column]s used in the SELECT statement.
         */
        fun columns(columns: Array<Column<*>>) = apply { this.columns = columns }

        /**
         * Sets the [Column]s of the record that must be retrieved.
         * The [Column] parameters are repeated to have an array with at least 1 [Column].
         *
         * @param first first [Column].
         * @param others other [Column]s that will be concatenated to the first one.
         */
        fun columns(first: Column<*>, vararg others: Column<*>) =
            apply { columns = first.plusArray(others) }

        /**
         * Helper method used to add the COUNT aggregate function to this SELECT statement
         * because usually it's widely used.
         */
        fun count() = apply { aggregates = arrayOf(Aggregate.count()) }

        /**
         * Sets the DISTINCT property used to eliminate all the duplicate records from the
         * result set of this statement and fetching only the unique records.
         */
        fun distinct() = apply { isDistinct = true }

        /**
         * Sets the CROSS JOIN clause on the SELECT statement.
         *
         * @param table name of the table that will be joined with the main table.
         * @see Join.cross
         */
        fun crossJoin(table: String) = apply { joins.add(Join.cross(table)) }

        /**
         * Sets the INNER JOIN clause on the SELECT statement.
         *
         * @param table name of the table that will be joined with the main table.
         * @param on raw [Expression] used to join the tables.
         * @see Join.inner
         */
        fun innerJoin(table: String, on: String) = apply { joins.add(Join.inner(table, on)) }

        /**
         * Sets the INNER JOIN clause on the SELECT statement.
         *
         * @param table name of the table that will be joined with the main table.
         * @param on [Expression] used to join the tables.
         * @see Join.inner
         */
        fun innerJoin(table: String, on: Expression) = apply { joins.add(Join.inner(table, on)) }

        /**
         * Sets the LEFT OUTER JOIN clause on the SELECT statement.
         *
         * @param table name of the table that will be joined with the main table.
         * @param on raw [Expression] used to join the tables.
         * @see Join.leftOuter
         */
        fun leftOuterJoin(table: String, on: String) =
            apply { joins.add(Join.leftOuter(table, on)) }

        /**
         * Sets the LEFT OUTER JOIN clause on the SELECT statement.
         *
         * @param table name of the table that will be joined with the main table.
         * @param on [Expression] used to join the tables.
         * @see Join.leftOuter
         */
        fun leftOuterJoin(table: String, on: Expression) =
            apply { joins.add(Join.leftOuter(table, on)) }

        /**
         * Sets the ORDER BY clause on the SELECT statement to specify the ascending order on one or
         * more columns.
         * The [Column] parameters are repeated to have an array with at least 1 [Column].
         *
         * @param first first [Column].
         * @param others other [Column]s that will be concatenated to the first one.
         * @see Order.asc
         */
        fun orderAsc(first: Column<*>, vararg others: Column<*>) =
            apply { orderBy = Order.asc(first, *others) }

        /**
         * Sets the ORDER BY clause on the SELECT statement to specify the descending order on one or
         * more columns.
         * The [Column] parameters are repeated to have an array with at least 1 [Column].
         *
         * @param first first [Column].
         * @param others other [Column]s that will be concatenated to the first one.
         * @see Order.desc
         */
        fun orderDesc(first: Column<*>, vararg others: Column<*>) =
            apply { orderBy = Order.desc(first, *others) }

        /**
         * Sets the LIMIT clause on the SELECT statement to specify the range of records that
         * must be retrieved.
         *
         * @param limit upper bound on the number of rows returned or updated.
         * @param offset lower bound on the number of rows returned or updated.
         * @see Limit
         */
        fun limit(limit: Int, offset: Int = 0) = apply { this.limit = Limit.of(limit, offset) }

        /**
         * Sets the GROUP BY clause on the SELECT statement to arrange identical data into groups.
         *
         * @param columns [Column]s containing the data that must be grouped.
         */
        fun groupBy(vararg columns: Column<*>) = apply { groupByColumns = columns }

        /**
         * Sets the WHERE clause to define additional conditions to retrieve records.
         * If not specified, all records in the table will be retrieved.
         *
         * @param expression instance of [Expression] that defines the WHERE clause.
         */
        fun where(expression: Expression) = apply { where = expression }

        /**
         * Sets the WHERE clause to define additional conditions to retrieve records.
         * If not specified, all records in the table will be retrieved.
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
         * Sets the raw clause used to identify the group from which the records will be retrieved.
         *
         * @param raw raw clause used to identify the group of the records.
         */
        internal fun from(raw: String) = apply { this.from = raw }

        /**
         * Creates a raw SELECT [Statement].
         *
         * @return [Statement] with a [QueryExecutor] that returns the [SQLiteColumnReader] used to read the records.
         */
        fun build(): SQLiteQueryStatement<QueryExecutor<SQLiteColumnReader>> {
            val atLeastOneColumn = !columns.isNullOrEmpty()
            val atLeastOneAggregate = !aggregates.isNullOrEmpty()

            if (!atLeastOneColumn && !atLeastOneAggregate)
                throw IllegalArgumentException("You have to specify the columns/aggregates to select.")

            val sql = StringBuilder("SELECT ")
            if (isDistinct) {
                sql.append("DISTINCT ")
            }

            if (atLeastOneColumn) {
                // Use always the alias on the columns.
                sql.append(columns!!.joinToString { "${it.fullName} AS \"${it.alias}\"" })
                if (atLeastOneAggregate)
                    sql.append(',')
            }

            if (atLeastOneAggregate) {
                // Use always the alias on the aggregate functions.
                sql.append(aggregates!!.joinToString { "${it.fullName} AS \"${it.alias}\"" })
            }

            sql.append(' ')
                .append("FROM(")
                .append(from)
                .append(')')

            joins.forEach { sql.append(it) }
            where?.let {
                sql.append("WHERE(")
                sql.append(it.raw())
                sql.append(')')
            }
            if (!groupByColumns.isNullOrEmpty()) {
                sql.append("GROUP BY (")
                    .append(groupByColumns!!.joinToString { it.fullName })
                    .append(')')
            }
            orderBy?.let { sql.append(it) }
            limit?.let { sql.append(it) }

            val args = where?.args() ?: emptyArray()
            return SQLiteQueryStatement.lined(
                sql.toString(),
                args
            ) { QueryExecutor(it) { SQLiteColumnReader(it) } }
        }
    }
}