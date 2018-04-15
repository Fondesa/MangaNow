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

package com.fondesa.data.database.extension

import com.fondesa.data.database.clause.ColumnExpression
import com.fondesa.data.database.clause.Expression
import com.fondesa.data.database.structure.Column

/**
 * Creates an [Expression] that matches only null values.
 *
 * @return instance of [ColumnExpression].
 */
fun Column<*>.isNull() = ColumnExpression(this, " IS NULL")

/**
 * Creates an [Expression] that matches only not null values.
 *
 * @return instance of [ColumnExpression].
 */
fun Column<*>.isNotNull() = ColumnExpression(this, " IS NOT NULL")

/**
 * Creates an [Expression] that matches values equal to [value].
 *
 * @param value value used for the matching. If the value is a string, the value mustn't be wrapped in quotes.
 * @return instance of [ColumnExpression].
 */
fun Column<*>.equalTo(value: Any) = ColumnExpression(this, " = ?", wrap(value))

/**
 * Creates an [Expression] that matches values equal to the values of [column].
 *
 * @param column [Column] containing values used for the matching.
 * @return instance of [ColumnExpression].
 */
fun Column<*>.equalTo(column: Column<*>) = ColumnExpression(this, " = " + column.fullName)

/**
 * Creates an [Expression] that matches values not equal to [value].
 *
 * @param value value used for the matching. If the value is a string, the value mustn't be wrapped in quotes.
 * @return instance of [ColumnExpression].
 */
fun Column<*>.notEqualTo(value: Any) = ColumnExpression(this, " <> ?", wrap(value))

/**
 * Creates an [Expression] that matches values not equal to the values of [column].
 *
 * @param column [Column] containing values used for the matching.
 * @return instance of [ColumnExpression].
 */
fun Column<*>.notEqualTo(column: Column<*>) = ColumnExpression(this, " <> " + column.fullName)

/**
 * Creates an [Expression] that matches values major than [value].
 *
 * @param value value used for the matching. If the value is a string, the value mustn't be wrapped in quotes.
 * @return instance of [ColumnExpression].
 */
fun Column<*>.majorThan(value: Any) = ColumnExpression(this, " > ?", wrap(value))

/**
 * Creates an [Expression] that matches values major than the values of [column].
 *
 * @param column [Column] containing values used for the matching.
 * @return instance of [ColumnExpression].
 */
fun Column<*>.majorThan(column: Column<*>) = ColumnExpression(this, " > " + column.fullName)

/**
 * Creates an [Expression] that matches values minor than [value].
 *
 * @param value value used for the matching. If the value is a string, the value mustn't be wrapped in quotes.
 * @return instance of [ColumnExpression].
 */
fun Column<*>.minorThan(value: Any) = ColumnExpression(this, " < " + wrap(value))

/**
 * Creates an [Expression] that matches values minor than the values of [column].
 *
 * @param column [Column] containing values used for the matching.
 * @return instance of [ColumnExpression].
 */
fun Column<*>.minorThan(column: Column<*>) = ColumnExpression(this, " < " + column.fullName)

/**
 * Creates an [Expression] that matches values major than or equal to [value].
 *
 * @param value value used for the matching. If the value is a string, the value mustn't be wrapped in quotes.
 * @return instance of [ColumnExpression].
 */
fun Column<*>.majorOrEqualThan(value: Any) = ColumnExpression(this, " >= ?", wrap(value))

/**
 * Creates an [Expression] that matches values major than or equal to the values of [column].
 *
 * @param column [Column] containing values used for the matching.
 * @return instance of [ColumnExpression].
 */
fun Column<*>.majorOrEqualThan(column: Column<*>) = ColumnExpression(this, " >= " + column.fullName)

/**
 * Creates an [Expression] that matches values minor than or equal to [value].
 *
 * @param value value used for the matching. If the value is a string, the value mustn't be wrapped in quotes.
 * @return instance of [ColumnExpression].
 */
fun Column<*>.minorOrEqualThan(value: Any) = ColumnExpression(this, " <= ?", wrap(value))

/**
 * Creates an [Expression] that matches values minor than or equal to the values of [column].
 *
 * @param column [Column] containing values used for the matching.
 * @return instance of [ColumnExpression].
 */
fun Column<*>.minorOrEqualThan(column: Column<*>) = ColumnExpression(this, " <= " + column.fullName)

/**
 * Creates an [Expression] that matches values against a pattern using wildcards.
 * The allowed wildcards are "_" and "%". They can be used in combinations and placed anywhere.
 *
 * @param value value used for the matching. If the value is a string, the value mustn't be wrapped in quotes.
 * @return instance of [ColumnExpression].
 */
fun Column<*>.like(value: Any) = ColumnExpression(this, " LIKE ?", wrap(value))

/**
 * Creates an [Expression] that matches values against a pattern using wildcards.
 * The allowed wildcards are "_" and "%". They can be used in combinations and placed anywhere.
 *
 * @param column [Column] containing values used for the matching.
 * @return instance of [ColumnExpression].
 */
fun Column<*>.like(column: Column<*>) = ColumnExpression(this, " LIKE " + column.fullName)

/**
 * Creates an [Expression] that matches values between [first] and [second].
 *
 * @param first lower bound value used for the matching. If the value is a string, the value mustn't be wrapped in quotes.
 * @param second upper bound value used for the matching. If the value is a string, the value mustn't be wrapped in quotes.
 * @return instance of [ColumnExpression].
 */
fun Column<*>.between(first: Any, second: Any) =
    ColumnExpression(this, " BETWEEN ? AND ?", wrap(first), wrap(second))

/**
 * Creates an [Expression] that matches values between the values of [first] and [second].
 *
 * @param first [Column] containing lower bound values used for the matching.
 * @param second [Column] containing upper bound values used for the matching.
 * @return instance of [ColumnExpression].
 */
fun Column<*>.between(first: Column<*>, second: Column<*>) =
    ColumnExpression(this, " BETWEEN " + first.fullName + " AND " + second.fullName)

/**
 * Creates an [Expression] that matches values equal to [values].
 * The IN condition is used to reduce the need to use multiple OR [Expression]s in statements.
 *
 * @param values values used for the matching. If the values are strings, the values mustn't be wrapped in quotes
 * @return instance of [ColumnExpression].
 */
fun Column<*>.`in`(vararg values: Any) = ColumnExpression(
    this,
    " IN (" + values.interpolateWith(',') { "?" } + ")",
    *values.map { wrap(it) }.toTypedArray()
)

/**
 * Creates an [Expression] that matches values equal to the values of [columns].
 * The IN condition is used to reduce the need to use multiple OR [Expression]s in statements.
 *
 * @param columns [Column]s containing values used for the matching.
 * @return instance of [ColumnExpression].
 */
fun Column<*>.`in`(vararg columns: Column<*>) =
    ColumnExpression(this, " IN (" + columns.interpolateWith(',') { it.fullName } + ")")

/**
 * Creates an [Expression] that matches values not equal to [values].
 * The NOT IN condition is used to reduce the need to use multiple OR [Expression]s in statements.
 *
 * @param values values used for the matching. If the values are strings, the values mustn't be wrapped in quotes
 * @return instance of [ColumnExpression].
 */
fun Column<*>.notIn(vararg values: Any) = ColumnExpression(
    this,
    " NOT IN (" + values.interpolateWith(',') { "?" } + ")",
    *values.map { wrap(it) }.toTypedArray()
)

/**
 * Creates an [Expression] that matches values not equal to the values of [columns].
 * The NOT IN condition is used to reduce the need to use multiple OR [Expression]s in statements.
 *
 * @param columns [Column]s containing values used for the matching.
 * @return instance of [ColumnExpression].
 */
fun Column<*>.notIn(vararg columns: Column<*>) =
    ColumnExpression(this, " NOT IN (" + columns.interpolateWith(',') { it.fullName } + ")")

private fun wrap(value: Any): String {
    return if (value is Boolean) {
        // Convert the boolean to the related integer.
        if (value) "1" else "0"
    } else value.toString()
}