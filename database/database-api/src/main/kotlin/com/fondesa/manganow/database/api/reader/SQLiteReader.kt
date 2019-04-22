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
 * Implementation of [DatabaseReader] using a [Cursor].
 *
 * @param cursor [Cursor] used to provide read access to the result set returned
 * by a database query.
 */
open class SQLiteReader(protected val cursor: Cursor) :
    DatabaseReader {

    override fun isNull(index: Int): Boolean = index == -1 || cursor.isNull(index)

    override fun getBoolean(index: Int): Boolean = getInt(index) != 0

    override fun getNullableBoolean(index: Int): Boolean? =
        if (isNull(index)) null else getBoolean(index)

    override fun getShort(index: Int): Short = cursor.getShort(index)

    override fun getNullableShort(index: Int): Short? = if (isNull(index)) null else getShort(index)

    override fun getInt(index: Int): Int = cursor.getInt(index)

    override fun getNullableInt(index: Int): Int? = if (isNull(index)) null else getInt(index)

    override fun getLong(index: Int): Long = cursor.getLong(index)

    override fun getNullableLong(index: Int): Long? = if (isNull(index)) null else getLong(index)

    override fun getFloat(index: Int): Float = cursor.getFloat(index)

    override fun getNullableFloat(index: Int): Float? = if (isNull(index)) null else getFloat(index)

    override fun getDouble(index: Int): Double = cursor.getDouble(index)

    override fun getNullableDouble(index: Int): Double? =
        if (isNull(index)) null else getDouble(index)

    override fun getString(index: Int): String = cursor.getString(index)

    override fun getNullableString(index: Int): String? =
        if (isNull(index)) null else getString(index)

    override fun getBlob(index: Int): ByteArray = cursor.getBlob(index)

    override fun getNullableBlob(index: Int): ByteArray? =
        if (isNull(index)) null else getBlob(index)
}