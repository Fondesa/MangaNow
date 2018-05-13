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

package com.fondesa.database.extension

import com.fondesa.common.database.clause.Expression
import com.fondesa.database.clause.ColumnExpression
import com.fondesa.database.structure.Column

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
 * @param T type of the values contained in the column.
 * @return instance of [ColumnExpression].
 */
fun <T : Any> Column<T>.equalTo(value: T) = ColumnExpression(this, " = ?", wrap(value))

/**
 * Creates an [Expression] that matches values equal to the values of [column].
 *
 * @param column [Column] containing values used for the matching.
 * @param T type of the values contained in the column.
 * @return instance of [ColumnExpression].
 */
fun <T : Any> Column<T>.equalTo(column: Column<T>) = ColumnExpression(this, " = " + column.fullName)

/**
 * Creates an [Expression] that matches values not equal to [value].
 *
 * @param value value used for the matching. If the value is a string, the value mustn't be wrapped in quotes.
 * @param T type of the values contained in the column.
 * @return instance of [ColumnExpression].
 */
fun <T : Any> Column<T>.notEqualTo(value: T) = ColumnExpression(this, " <> ?", wrap(value))

/**
 * Creates an [Expression] that matches values not equal to the values of [column].
 *
 * @param column [Column] containing values used for the matching.
 * @param T type of the values contained in the column.
 * @return instance of [ColumnExpression].
 */
fun <T : Any> Column<T>.notEqualTo(column: Column<T>) =
    ColumnExpression(this, " <> " + column.fullName)

/**
 * Creates an [Expression] that matches values major than [value].
 *
 * @param value value used for the matching. If the value is a string, the value mustn't be wrapped in quotes.
 * @param T type of the values contained in the column.
 * @return instance of [ColumnExpression].
 */
fun <T : Any> Column<T>.majorThan(value: T) = ColumnExpression(this, " > ?", wrap(value))

/**
 * Creates an [Expression] that matches values major than the values of [column].
 *
 * @param column [Column] containing values used for the matching.
 * @param T type of the values contained in the column.
 * @return instance of [ColumnExpression].
 */
fun <T : Any> Column<T>.majorThan(column: Column<T>) =
    ColumnExpression(this, " > " + column.fullName)

/**
 * Creates an [Expression] that matches values minor than [value].
 *
 * @param value value used for the matching. If the value is a string, the value mustn't be wrapped in quotes.
 * @param T type of the values contained in the column.
 * @return instance of [ColumnExpression].
 */
fun <T : Any> Column<T>.minorThan(value: T) = ColumnExpression(this, " < " + wrap(value))

/**
 * Creates an [Expression] that matches values minor than the values of [column].
 *
 * @param column [Column] containing values used for the matching.
 * @param T type of the values contained in the column.
 * @return instance of [ColumnExpression].
 */
fun <T : Any> Column<T>.minorThan(column: Column<T>) =
    ColumnExpression(this, " < " + column.fullName)

/**
 * Creates an [Expression] that matches values major than or equal to [value].
 *
 * @param value value used for the matching. If the value is a string, the value mustn't be wrapped in quotes.
 * @param T type of the values contained in the column.
 * @return instance of [ColumnExpression].
 */
fun <T : Any> Column<T>.majorOrEqualThan(value: T) = ColumnExpression(this, " >= ?", wrap(value))

/**
 * Creates an [Expression] that matches values major than or equal to the values of [column].
 *
 * @param column [Column] containing values used for the matching.
 * @param T type of the values contained in the column.
 * @return instance of [ColumnExpression].
 */
fun <T : Any> Column<T>.majorOrEqualThan(column: Column<T>) =
    ColumnExpression(this, " >= " + column.fullName)

/**
 * Creates an [Expression] that matches values minor than or equal to [value].
 *
 * @param value value used for the matching. If the value is a string, the value mustn't be wrapped in quotes.
 * @param T type of the values contained in the column.
 * @return instance of [ColumnExpression].
 */
fun <T : Any> Column<T>.minorOrEqualThan(value: T) = ColumnExpression(this, " <= ?", wrap(value))

/**
 * Creates an [Expression] that matches values minor than or equal to the values of [column].
 *
 * @param column [Column] containing values used for the matching.
 * @param T type of the values contained in the column.
 * @return instance of [ColumnExpression].
 */
fun <T : Any> Column<T>.minorOrEqualThan(column: Column<T>) =
    ColumnExpression(this, " <= " + column.fullName)

/**
 * Creates an [Expression] that matches values against a pattern using wildcards.
 * The allowed wildcards are "_" and "%". They can be used in combinations and placed anywhere.
 *
 * @param value value used for the matching. If the value is a string, the value mustn't be wrapped in quotes.
 * @param T type of the values contained in the column.
 * @return instance of [ColumnExpression].
 */
fun <T : Any> Column<T>.like(value: T) = ColumnExpression(this, " LIKE ?", wrap(value))

/**
 * Creates an [Expression] that matches values against a pattern using wildcards.
 * The allowed wildcards are "_" and "%". They can be used in combinations and placed anywhere.
 *
 * @param column [Column] containing values used for the matching.
 * @param T type of the values contained in the column.
 * @return instance of [ColumnExpression].
 */
fun <T : Any> Column<T>.like(column: Column<T>) = ColumnExpression(this, " LIKE " + column.fullName)

/**
 * Creates an [Expression] that matches values between [first] and [second].
 *
 * @param first lower bound value used for the matching. If the value is a string, the value mustn't be wrapped in quotes.
 * @param second upper bound value used for the matching. If the value is a string, the value mustn't be wrapped in quotes.
 * @param T type of the values contained in the column.
 * @return instance of [ColumnExpression].
 */
fun <T : Any> Column<T>.between(first: T, second: T) =
    ColumnExpression(this, " BETWEEN ? AND ?", wrap(first), wrap(second))

/**
 * Creates an [Expression] that matches values between the values of [first] and [second].
 *
 * @param first [Column] containing lower bound values used for the matching.
 * @param second [Column] containing upper bound values used for the matching.
 * @param T type of the values contained in the column.
 * @return instance of [ColumnExpression].
 */
fun <T : Any> Column<T>.between(first: Column<T>, second: Column<T>) =
    ColumnExpression(this, " BETWEEN " + first.fullName + " AND " + second.fullName)

/**
 * Creates an [Expression] that matches values equal to [values].
 * The IN condition is used to reduce the need to use multiple OR [Expression]s in statements.
 *
 * @param values values used for the matching. If the values are strings, the values mustn't be wrapped in quotes.
 * @param T type of the values contained in the column.
 * @return instance of [ColumnExpression].
 */
fun <T : Any> Column<T>.within(vararg values: T) = ColumnExpression(
    this,
    " IN (" + values.joinToString { "?" } + ")",
    *values.map { wrap(it) }.toTypedArray()
)

/**
 * Creates an [Expression] that matches values equal to the values of [columns].
 * The IN condition is used to reduce the need to use multiple OR [Expression]s in statements.
 *
 * @param columns [Column]s containing values used for the matching.
 * @param T type of the values contained in the column.
 * @return instance of [ColumnExpression].
 */
fun <T : Any> Column<T>.within(vararg columns: Column<T>) =
    ColumnExpression(this, " IN (" + columns.joinToString { it.fullName } + ")")

/**
 * Creates an [Expression] that matches values not equal to [values].
 * The NOT IN condition is used to reduce the need to use multiple OR [Expression]s in statements.
 *
 * @param values values used for the matching. If the values are strings, the values mustn't be wrapped in quotes.
 * @param T type of the values contained in the column.
 * @return instance of [ColumnExpression].
 */
fun <T : Any> Column<T>.notWithin(vararg values: T) = ColumnExpression(
    this,
    " NOT IN (" + values.joinToString { "?" } + ")",
    *values.map { wrap(it) }.toTypedArray()
)

/**
 * Creates an [Expression] that matches values not equal to the values of [columns].
 * The NOT IN condition is used to reduce the need to use multiple OR [Expression]s in statements.
 *
 * @param columns [Column]s containing values used for the matching.
 * @param T type of the values contained in the column.
 * @return instance of [ColumnExpression].
 */
fun <T : Any> Column<T>.notWithin(vararg columns: Column<T>) =
    ColumnExpression(this, " NOT IN (" + columns.joinToString { it.fullName } + ")")

private fun wrap(value: Any): String {
    return if (value is Boolean) {
        // Convert the boolean to the related integer.
        if (value) "1" else "0"
    } else value.toString()
}