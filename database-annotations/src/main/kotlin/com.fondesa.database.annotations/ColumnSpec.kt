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

package com.fondesa.database.annotations

/**
 * Builder used to create a column in a table.
 *
 * @param tableName name of the table that contains this column.
 * @param name name of the column.
 * @param type type of the column (one of [Type]).
 */
open class ColumnSpec<ValueType>(
    val name: String,
    val type: Type
) {

    var isPrimaryKey: Boolean = false
    var isUnique: Boolean = false
    var isNotNull: Boolean = false
    var defaultValue: ValueType? = null

    /**
     * Sets this column as primary key.
     * Automatically this column will be not null also if SQLite supports it for legacy builds.
     * A SQLite from can't use more than one primary key, so, if in the same [Table] there's more
     * than one column used as primary key, the primary key will be composite.
     */
    fun primaryKey() = apply {
        isPrimaryKey = true
        notNull()
    }

    /**
     * Sets this column as unique.
     * Automatically this column will be not null also if SQLite supports it for legacy builds.
     * A SQLite from can use more than one unique column.
     */
    fun unique() = apply {
        isUnique = true
        notNull()
    }

    /**
     * Sets this column as not null.
     * If the column is set as not null and a null value is inserted, it will result as an exception.
     */
    fun notNull() = apply { isNotNull = true }

    /**
     * Default value to use when it isn't set explicitly.
     * This value can't be null.
     *
     * @param defaultValue value set as default in this column when it isn't bound manually.
     */
    fun defaultValue(defaultValue: ValueType) = apply { this.defaultValue = defaultValue }


    enum class Type(val value: String) {

        /**
         * This type allows signed integer values, stored in 1, 2, 3, 4, 6, or 8 bytes,
         * depending on the magnitude of the value.
         */
        INTEGER("integer"),

        /**
         * This type allows floating point values, stored as 8-byte IEEE floating point numbers.
         */
        REAL("real"),

        /**
         * This type allows strings, stored using the database encoding (UTF-8, UTF-16BE or UTF-16LE).
         */
        TEXT("text"),

        /**
         * This type allows blobs of data, stored exactly as it was input.
         */
        BLOB("blob")
    }

    companion object {

        /**
         * Name of the inner autoincrement column of SQLite.
         */
        const val ROW_ID_NAME = "rowid"

        private const val SEPARATOR = '@'
    }
}

/**
 * Builder used to create a floating-point column in a table.
 */
class RealColumnSpec(name: String) :
    ColumnSpec<Double>(name, ColumnSpec.Type.REAL)

/**
 * Builder used to create an integer or a boolean column in a table.
 */
class IntegerColumnSpec(name: String) :
    ColumnSpec<Long>(name, ColumnSpec.Type.INTEGER)

/**
 * Builder used to create a string column in a table.
 */
class TextColumnSpec(name: String) :
    ColumnSpec<String>(name, ColumnSpec.Type.TEXT)

/**
 * Builder used to create a byte array column in a table.
 */
class BlobColumnSpec(name: String) :
    ColumnSpec<ByteArray>(name, ColumnSpec.Type.BLOB)