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

package com.fondesa.manganow.database.api.client.reader

/**
 * Layer used to read data from a database query's result.
 * Records' values are accessed through column's index.
 */
interface DatabaseReader {

    /**
     * Check if the value of the requested column is null.
     *
     * @param index the zero-based index of the target column.
     * @return true if the value of that column is null.
     */
    fun isNull(index: Int): Boolean

    /**
     * Get the value of the requested column as a boolean.
     * Values of type <i>boolean</i> are expressed through an integer and their values are:
     * <ul>
     * <li>false: if the value is 0</li>
     * <li>true: if the value is not 0</li>
     * </ul>
     *
     * @param index the zero-based index of the target column.
     * @return the value of that column as a boolean.
     */
    fun getBoolean(index: Int): Boolean

    /**
     * Get the value of the requested column as a boolean or null if the value is null.
     * Values of type <i>boolean</i> are expressed through an integer and their values are:
     * <ul>
     * <li>false: if the value is 0</li>
     * <li>true: if the value is not 0</li>
     * </ul>
     *
     * @param index the zero-based index of the target column.
     * @return the value of that column as a boolean or null.
     */
    fun getNullableBoolean(index: Int): Boolean?

    /**
     * Get the value of the requested column as a short.
     *
     * @param index the zero-based index of the target column.
     * @return the value of that column as a short.
     */
    fun getShort(index: Int): Short

    /**
     * Get the value of the requested column as a short or null if the value is null.
     *
     * @param index the zero-based index of the target column.
     * @return the value of that column as a short or null.
     */
    fun getNullableShort(index: Int): Short?

    /**
     * Get the value of the requested column as an int.
     *
     * @param index the zero-based index of the target column.
     * @return the value of that column as an int.
     */
    fun getInt(index: Int): Int

    /**
     * Get the value of the requested column as an int or null if the value is null.
     *
     * @param index the zero-based index of the target column.
     * @return the value of that column as an int or null.
     */
    fun getNullableInt(index: Int): Int?

    /**
     * Get the value of the requested column as a long.
     *
     * @param index the zero-based index of the target column.
     * @return the value of that column as a long.
     */
    fun getLong(index: Int): Long

    /**
     * Get the value of the requested column as a long or null if the value is null.
     *
     * @param index the zero-based index of the target column.
     * @return the value of that column as a long or null.
     */
    fun getNullableLong(index: Int): Long?

    /**
     * Get the value of the requested column as a float.
     *
     * @param index the zero-based index of the target column.
     * @return the value of that column as a float.
     */
    fun getFloat(index: Int): Float

    /**
     * Get the value of the requested column as a float or null if the value is null.
     *
     * @param index the zero-based index of the target column.
     * @return the value of that column as a float or null.
     */
    fun getNullableFloat(index: Int): Float?

    /**
     * Get the value of the requested column as a double.
     *
     * @param index the zero-based index of the target column.
     * @return the value of that column as a double.
     */
    fun getDouble(index: Int): Double

    /**
     * Get the value of the requested column as a double or null if the value is null.
     *
     * @param index the zero-based index of the target column.
     * @return the value of that column as a double or null.
     */
    fun getNullableDouble(index: Int): Double?

    /**
     * Get the value of the requested column as a string.
     *
     * @param index the zero-based index of the target column.
     * @return the value of that column as a string.
     */
    fun getString(index: Int): String

    /**
     * Get the value of the requested column as a string or null if the value is null.
     *
     * @param index the zero-based index of the target column.
     * @return the value of that column as a string or null.
     */
    fun getNullableString(index: Int): String?

    /**
     * Get the value of the requested column as a byte array.
     *
     * @param index the zero-based index of the target column.
     * @return the value of that column as a string.
     */
    fun getBlob(index: Int): ByteArray

    /**
     * Get the value of the requested column as a byte array or null if the value is null.
     *
     * @param index the zero-based index of the target column.
     * @return the value of that column as a byte array or null.
     */
    fun getNullableBlob(index: Int): ByteArray?
}