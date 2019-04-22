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

package com.fondesa.manganow.database.api.reader

import android.database.Cursor

/**
 * Implementation of [SQLiteReader] used to read data returned from a raw query.
 * The reading can be done using the column's index or the column's name.
 * The column's name used to read the value is the same of the one specified in the raw query.
 * This means that if a column has an alias in the query, its value must be accessed through the
 * same alias and not through the complete column's name.
 * The reading using column's name will be slower than using the index.
 * A good strategy to maintain a good performance overall
 * would be preparing the indexes before the reading using [indexOf].
 *
 * @param cursor [Cursor] used to provide read access to the result set returned
 * by a database query.
 */
open class SQLiteRawReader(cursor: Cursor) : SQLiteReader(cursor) {

    /**
     * Obtain the index of the requested column using the column's name.
     *
     * @param columnName name of the target column specified in the raw query.
     * @return index of the column in the result set.
     */
    fun indexOf(columnName: String) = cursor.getColumnIndex(columnName)

    /**
     * Check if the value of the requested column is null.
     *
     * @param columnName name of the target column specified in the raw query.
     * @return true if the value of that column is null.
     */
    fun isNull(columnName: String) = isNull(indexOf(columnName))

    /**
     * Get the value of the requested column as a boolean.
     * Values of type <i>boolean</i> are expressed through an integer and their values are:
     * <ul>
     * <li>false: if the value is 0</li>
     * <li>true: if the value is not 0</li>
     * </ul>
     *
     * @param columnName name of the target column specified in the raw query.
     * @return the value of that column as a boolean.
     */
    fun getBoolean(columnName: String) = getBoolean(indexOf(columnName))

    /**
     * Get the value of the requested column as a boolean or null if the value is null.
     * Values of type <i>boolean</i> are expressed through an integer and their values are:
     * <ul>
     * <li>false: if the value is 0</li>
     * <li>true: if the value is not 0</li>
     * </ul>
     *
     * @param columnName name of the target column specified in the raw query.
     * @return the value of that column as a boolean or null.
     */
    fun getNullableBoolean(columnName: String) = getNullableBoolean(indexOf(columnName))

    /**
     * Get the value of the requested column as a short.
     *
     * @param columnName name of the target column specified in the raw query.
     * @return the value of that column as a short.
     */
    fun getShort(columnName: String) = getShort(indexOf(columnName))

    /**
     * Get the value of the requested column as a short or null if the value is null.
     *
     * @param columnName name of the target column specified in the raw query.
     * @return the value of that column as a short or null.
     */
    fun getNullableShort(columnName: String) = getNullableShort(indexOf(columnName))

    /**
     * Get the value of the requested column as an int.
     *
     * @param columnName name of the target column specified in the raw query.
     * @return the value of that column as an int.
     */
    fun getInt(columnName: String) = getInt(indexOf(columnName))

    /**
     * Get the value of the requested column as an int or null if the value is null.
     *
     * @param columnName name of the target column specified in the raw query.
     * @return the value of that column as an int or null.
     */
    fun getNullableInt(columnName: String) = getNullableInt(indexOf(columnName))

    /**
     * Get the value of the requested column as a long.
     *
     * @param columnName name of the target column specified in the raw query.
     * @return the value of that column as a long.
     */
    fun getLong(columnName: String) = getLong(indexOf(columnName))

    /**
     * Get the value of the requested column as a long or null if the value is null.
     *
     * @param columnName name of the target column specified in the raw query.
     * @return the value of that column as a long or null.
     */
    fun getNullableLong(columnName: String) = getNullableLong(indexOf(columnName))

    /**
     * Get the value of the requested column as a float.
     *
     * @param columnName name of the target column specified in the raw query.
     * @return the value of that column as a float.
     */
    fun getFloat(columnName: String) = getFloat(indexOf(columnName))

    /**
     * Get the value of the requested column as a float or null if the value is null.
     *
     * @param columnName name of the target column specified in the raw query.
     * @return the value of that column as a float or null.
     */
    fun getNullableFloat(columnName: String) = getNullableFloat(indexOf(columnName))

    /**
     * Get the value of the requested column as a double.
     *
     * @param columnName name of the target column specified in the raw query.
     * @return the value of that column as a double.
     */
    fun getDouble(columnName: String) = getDouble(indexOf(columnName))

    /**
     * Get the value of the requested column as a double or null if the value is null.
     *
     * @param columnName name of the target column specified in the raw query.
     * @return the value of that column as a double or null.
     */
    fun getNullableDouble(columnName: String) = getNullableDouble(indexOf(columnName))

    /**
     * Get the value of the requested column as a string.
     *
     * @param columnName name of the target column specified in the raw query.
     * @return the value of that column as a string.
     */
    fun getString(columnName: String) = getString(indexOf(columnName))

    /**
     * Get the value of the requested column as a string or null if the value is null.
     *
     * @param columnName name of the target column specified in the raw query.
     * @return the value of that column as a string or null.
     */
    fun getNullableString(columnName: String) = getNullableString(indexOf(columnName))

    /**
     * Get the value of the requested column as a byte array.
     *
     * @param columnName name of the target column specified in the raw query.
     * @return the value of that column as a string.
     */
    fun getBlob(columnName: String): ByteArray = getBlob(indexOf(columnName))

    /**
     * Get the value of the requested column as a byte array or null if the value is null.
     *
     * @param columnName name of the target column specified in the raw query.
     * @return the value of that column as a byte array or null.
     */
    fun getNullableBlob(columnName: String) = getNullableBlob(indexOf(columnName))
}