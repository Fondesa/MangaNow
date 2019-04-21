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

package com.fondesa.database.clause

import com.fondesa.database.api.clause.Expression

/**
 * Define the JOIN clause of SQLite.
 * The JOIN clause is used to combine records from two or more tables
 * using values common to each one.
 */
object Join {

    /**
     * Creates the raw CROSS JOIN clause.
     * The CROSS JOIN clause will produce the Cartesian product of the involved tables.
     * The number of rows in the result set will be equal to the product of
     * the number of rows in each involved table.
     *
     * @param table name of the table that will be joined with the main table.
     * @return raw CROSS JOIN clause.
     */
    fun cross(table: String) = raw(Type.CROSS, table, null)

    /**
     * Creates the raw INNER JOIN clause.
     * The INNER JOIN clause will return all rows from both tables if and only if both tables
     * meet the conditions specified in the [on] clause.
     *
     * @param table name of the table that will be joined with the main table.
     * @param on raw [Expression] used to join the tables.
     * @return raw INNER JOIN clause.
     */
    fun inner(table: String, on: String) = raw(Type.INNER, table, on)

    /**
     * Creates the raw INNER JOIN clause.
     * The INNER JOIN clause will return all rows from both tables if and only if both tables
     * meet the conditions specified in the [on] clause.
     *
     * @param table name of the table that will be joined with the main table.
     * @param on [Expression] used to join the tables.
     * @return raw INNER JOIN clause.
     */
    fun inner(table: String, on: Expression) = inner(table, on.raw())

    /**
     * Creates the raw LEFT OUTER JOIN clause.
     * The LEFT OUTER JOIN clause will return all rows from both tables that
     * meet the conditions specified in the [on] clause plus all rows from the main table
     * unmatched with [table] (in this last case, the columns' value of [table] will be null).
     *
     * @param table name of the table that will be joined with the main table.
     * @param on raw [Expression] used to join the tables.
     * @return raw LEFT OUTER JOIN clause.
     */
    fun leftOuter(table: String, on: String) = raw(Type.LEFT_OUTER, table, on)

    /**
     * Creates the raw LEFT OUTER JOIN clause.
     * The LEFT OUTER JOIN clause will return all rows from both tables that
     * meet the conditions specified in the [on] clause plus all rows from the main table
     * unmatched with [table] (in this last case, the columns' value of [table] will be null).
     *
     * @param table name of the table that will be joined with the main table.
     * @param on [Expression] used to join the tables.
     * @return raw LEFT OUTER JOIN clause.
     */
    fun leftOuter(table: String, on: Expression) = leftOuter(table, on.raw())

    private fun raw(type: Type, table: String, on: String?): String {
        val sb = StringBuilder(type.value)
            .append(" JOIN ")
            .append(table)

        on?.let {
            sb.append(" ON(")
                .append(it)
                .append(')')
        }
        return sb.toString()
    }

    enum class Type(val value: String) {

        /**
         * Used to define a cross join.
         */
        CROSS("CROSS"),

        /**
         * Used to define an inner join.
         */
        INNER("INNER"),

        /**
         * Used to define an outer join.
         */
        LEFT_OUTER("LEFT OUTER")
    }
}