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

import com.fondesa.database.extension.plusArray
import com.fondesa.database.structure.Column

/**
 * Define the ORDER BY clause of SQLite.
 * The ORDER BY clause is used to sort the data in ascending or descending
 * order, based on one or more columns.
 */
object Order {

    /**
     * Creates the raw ORDER BY clause.
     * The data-set will be sorted in an ascending way.
     * The [Column] parameters are repeated to have an array of at least 1 [Column].
     *
     * @param first first [Column] used to sort the data.
     * @param others others [Column] used to sort the data.
     * @return raw ORDER BY clause.
     */
    fun asc(first: Column<*>, vararg others: Column<*>) =
        raw(Type.ASCENDING, first.plusArray(others))

    /**
     * Creates the raw ORDER BY clause.
     * The data-set will be sorted in a descending way.
     * The [Column] parameters are repeated to have an array of at least 1 [Column].
     *
     * @param first first [Column] used to sort the data.
     * @param others others [Column] used to sort the data.
     * @return raw ORDER BY clause.
     */
    fun desc(first: Column<*>, vararg others: Column<*>) =
        raw(Type.DESCENDING, first.plusArray(others))

    private fun raw(type: Type, columns: Array<out Column<*>>): String {
        return StringBuilder("ORDER BY ")
            .append(columns.joinToString { it.fullName })
            .append(' ')
            .append(type.value)
            .toString()
    }

    enum class Type(val value: String) {

        /**
         * Used to define an ascending order.
         */
        ASCENDING("ASC"),

        /**
         * Used to define a descending order.
         */
        DESCENDING("DESC")
    }
}