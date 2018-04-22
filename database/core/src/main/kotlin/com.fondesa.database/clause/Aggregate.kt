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

import com.fondesa.database.structure.Column

/**
 * Define a SQLite aggregate function.
 * An aggregate function can be used inside a SELECT statement to perform processing
 * on string or numeric columns' values.
 *
 * @param fullName raw name used to declare the function in the SELECT statement.
 * @param alias SQLite alias used in the SELECT statement to recognize the
 * column related to the aggregate function.
 */
class Aggregate private constructor(val fullName: String, val alias: String) {

    /**
     * Builder used to create an instance of [Aggregate] function.
     *
     * @param name name of the aggregate function (e.g. count, avg, min, max)
     */
    class Builder(private val name: String) {

        private var alias: String? = null
        private var args: Array<out String>? = null

        /**
         * SQLite alias used in the SELECT statement to recognize the
         * column related to the aggregate function.
         *
         * @param alias alias related to the aggregate function.
         */
        fun alias(alias: String) = apply { this.alias = alias }

        /**
         * Add the arguments to this function.
         * If multiple arguments are passed, they will be separated by a comma inside the
         * declaration of the aggregate function.
         *
         * @param arguments arguments passed to this aggregate function.
         * @param transform optional transformation block that will be invoked on each argument
         * to transform it to a string.
         */
        fun <T> arguments(vararg arguments: T, transform: (T) -> String = { it.toString() }) =
            apply {
                args = arguments.map { transform(it) }.toTypedArray()
            }

        /**
         * Creates an instance of [Aggregate] using the parameters of this [Builder].
         * This method will additionally check if the parameters set on this [Builder] are valid.
         *
         * @return instance of [Aggregate].
         */
        fun build(): Aggregate {
            val args =
                args ?: throw IllegalArgumentException("You must specify at least an argument.")
            val alias = alias ?: throw IllegalArgumentException("You must specify the alias.")

            val sql = StringBuilder(name)
                .append('(')
                .append(args.joinToString())
                .append(')')

            return Aggregate(sql.toString(), alias)
        }
    }

    companion object {

        /**
         * Creates a SQLite avg() function.
         * This function will return the average value of all non-null values of [column]
         * within a group. Text and blob values will be interpreted as 0. The result of avg()
         * will be a floating point value if there's at least one non-null value.
         * If all values are null, the result will be null.
         *
         * @param column [Column] on which the function will be invoked.
         * @return instance of [Aggregate] function related to SQLite avg() function.
         */
        fun avg(column: Column<*>) = aggregateSingleColumn("avg", column)

        /**
         * Creates a SQLite count() function.
         * This function will return the total number of rows in the group.
         *
         * @return instance of [Aggregate] function related to SQLite count() function.
         */
        fun count() = Builder("count")
            .arguments("*")
            .alias("count_all")
            .build()

        /**
         * Creates a SQLite count() function.
         * This function will return the count of the number of times that
         * the value of [column] is not null in a group.
         *
         * @param column [Column] on which the function will be invoked.
         * @return instance of [Aggregate] function related to SQLite count() function.
         */
        fun count(column: Column<*>) = aggregateSingleColumn("count", column)

        /**
         * Creates a SQLite group_concat() function.
         * This function will return a string which is the concatenation of all non-null
         * values of [column] interpolated by the [separator] string.
         * If the [separator] string is not passed to the function, a comma will be used.
         *
         * @param column [Column] on which the function will be invoked.
         * @param separator string separator used to interpolate the values of [column]
         * @return instance of [Aggregate] function related to SQLite group_concat() function.
         */
        fun groupConcat(column: Column<*>, separator: String? = null): Aggregate {
            val fullName = column.fullName
            val args = if (separator == null)
                arrayOf(fullName)
            else
                arrayOf(fullName, separator)

            return Builder("group_concat")
                .arguments(args)
                .alias(column.alias)
                .build()
        }

        /**
         * Creates a SQLite max() function.
         * This function will return the maximum value of all values of [column] in the group.
         * If all values of [column] are null, this aggregate function will return null.
         *
         * @param column [Column] on which the function will be invoked.
         * @return instance of [Aggregate] function related to SQLite max() function.
         */
        fun max(column: Column<*>) = aggregateSingleColumn("max", column)

        /**
         * Creates a SQLite min() function.
         * This function will return the minimum value of all values of [column] in the group.
         * If all values of [column] are null, this aggregate function will return null.
         *
         * @param column [Column] on which the function will be invoked.
         * @return instance of [Aggregate] function related to SQLite min() function.
         */
        fun min(column: Column<*>) = aggregateSingleColumn("min", column)

        /**
         * Creates a SQLite sum() function.
         * This function will return the sum of all non-null values of [column] in the group.
         * If all values of [column] are null, this aggregate function will return null.
         *
         * @param column [Column] on which the function will be invoked.
         * @return instance of [Aggregate] function related to SQLite sum() function.
         */
        fun sum(column: Column<*>) = aggregateSingleColumn("sum", column)

        /**
         * Creates a SQLite total() function.
         * This function will return the sum of all non-null values of [column] in the group.
         * If all values of [column] are null, this aggregate function will return 0.0.
         *
         * @param column [Column] on which the function will be invoked.
         * @return instance of [Aggregate] function related to SQLite total() function.
         */
        fun total(column: Column<*>) = aggregateSingleColumn("total", column)

        private fun aggregateSingleColumn(name: String, column: Column<*>) = Builder(name)
            .arguments(column.fullName)
            .alias(column.alias)
            .build()
    }
}
