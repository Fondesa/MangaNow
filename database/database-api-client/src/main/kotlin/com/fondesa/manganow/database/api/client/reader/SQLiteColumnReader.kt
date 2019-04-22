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

package com.fondesa.manganow.database.api.client.reader

import android.database.Cursor
import com.fondesa.manganow.database.api.structure.Column

/**
 * Implementation of [SQLiteReader] used to read data returned from a query
 * that specified the columns as [Column].
 * The reading can be done using the column's index or the [Column]'s reference.
 * The column's name used to read the value is the default alias assigned to each [Column].
 * The reading using column's reference will be slower than using the index.
 * A good strategy to maintain a good performance overall
 * would be preparing the indexes before the reading using [indexOf].
 *
 * @param cursor [Cursor] used to provide read access to the result set returned
 * by a database query.
 */
class SQLiteColumnReader(cursor: Cursor) : SQLiteRawReader(cursor) {

    /**
     * Obtain the index of the requested column using the column's name.
     *
     * @param column reference of the target column.
     * @return index of the column in the result set.
     */
    fun indexOf(column: Column<*>) = cursor.getColumnIndex(column.alias)

    /**
     * Check if the value of the requested column is null.
     *
     * @param column reference of the target column.
     * @return true if the value of that column is null.
     */
    fun isNull(column: Column<*>) = isNull(indexOf(column))

    /**
     * Get the value of the requested column as a boolean.
     * Values of type <i>boolean</i> are expressed through an integer and their values are:
     * <ul>
     * <li>false: if the value is 0</li>
     * <li>true: if the value is not 0</li>
     * </ul>
     *
     * @param column reference of the target column.
     * @return the value of that column as a boolean.
     */
    fun getBoolean(column: Column<*>) = getBoolean(indexOf(column))

    /**
     * Get the value of the requested column as a boolean or null if the value is null.
     * Values of type <i>boolean</i> are expressed through an integer and their values are:
     * <ul>
     * <li>false: if the value is 0</li>
     * <li>true: if the value is not 0</li>
     * </ul>
     *
     * @param column reference of the target column.
     * @return the value of that column as a boolean or null.
     */
    fun getNullableBoolean(column: Column<*>) = getNullableBoolean(indexOf(column))

    /**
     * Get the value of the requested column as a short.
     *
     * @param column reference of the target column.
     * @return the value of that column as a short.
     */
    fun getShort(column: Column<*>) = getShort(indexOf(column))

    /**
     * Get the value of the requested column as a short or null if the value is null.
     *
     * @param column reference of the target column.
     * @return the value of that column as a short or null.
     */
    fun getNullableShort(column: Column<*>) = getNullableShort(indexOf(column))

    /**
     * Get the value of the requested column as an int.
     *
     * @param column reference of the target column.
     * @return the value of that column as an int.
     */
    fun getInt(column: Column<*>) = getInt(indexOf(column))

    /**
     * Get the value of the requested column as an int or null if the value is null.
     *
     * @param column reference of the target column.
     * @return the value of that column as an int or null.
     */
    fun getNullableInt(column: Column<*>) = getNullableInt(indexOf(column))

    /**
     * Get the value of the requested column as a long.
     *
     * @param column reference of the target column.
     * @return the value of that column as a long.
     */
    fun getLong(column: Column<*>) = getLong(indexOf(column))

    /**
     * Get the value of the requested column as a long or null if the value is null.
     *
     * @param column reference of the target column.
     * @return the value of that column as a long or null.
     */
    fun getNullableLong(column: Column<*>) = getNullableLong(indexOf(column))

    /**
     * Get the value of the requested column as a float.
     *
     * @param column reference of the target column.
     * @return the value of that column as a float.
     */
    fun getFloat(column: Column<*>) = getFloat(indexOf(column))

    /**
     * Get the value of the requested column as a float or null if the value is null.
     *
     * @param column reference of the target column.
     * @return the value of that column as a float or null.
     */
    fun getNullableFloat(column: Column<*>) = getNullableFloat(indexOf(column))

    /**
     * Get the value of the requested column as a double.
     *
     * @param column reference of the target column.
     * @return the value of that column as a double.
     */
    fun getDouble(column: Column<*>) = getDouble(indexOf(column))

    /**
     * Get the value of the requested column as a double or null if the value is null.
     *
     * @param column reference of the target column.
     * @return the value of that column as a double or null.
     */
    fun getNullableDouble(column: Column<*>) = getNullableDouble(indexOf(column))

    /**
     * Get the value of the requested column as a string.
     *
     * @param column reference of the target column.
     * @return the value of that column as a string.
     */
    fun getString(column: Column<*>): String = getString(indexOf(column))

    /**
     * Get the value of the requested column as a string or null if the value is null.
     *
     * @param column reference of the target column.
     * @return the value of that column as a string or null.
     */
    fun getNullableString(column: Column<*>) = getNullableString(indexOf(column))

    /**
     * Get the value of the requested column as a byte array.
     *
     * @param column reference of the target column.
     * @return the value of that column as a string.
     */
    fun getBlob(column: Column<*>): ByteArray = getBlob(indexOf(column))

    /**
     * Get the value of the requested column as a byte array or null if the value is null.
     *
     * @param column reference of the target column.
     * @return the value of that column as a byte array or null.
     */
    fun getNullableBlob(column: Column<*>) = getNullableBlob(indexOf(column))
}