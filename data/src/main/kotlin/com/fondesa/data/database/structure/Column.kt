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

package com.fondesa.data.database.structure

import com.fondesa.data.database.structure.Column.Type

/**
 * Class used to specify the properties of a SQLite column in a from.
 *
 * @param tableName name of the table that contains this column.
 * @param name name of the column.
 * @param type type of the column (one of [Type]).
 * @param isPrimaryKey true if the column is considered as a primary key or is a part of a
 * composite primary key with others columns.
 * @param isUnique true if the column's values will be unique.
 * @param isNotNull true if the column is declared as not null.
 * @param defaultValue value assigned when the value would have been null.
 * @param ValueType type of the value that will be inserted in the column
 */
class Column<out ValueType>(
    val tableName: String,
    val name: String,
    val type: Type,
    val isPrimaryKey: Boolean,
    val isUnique: Boolean,
    val isNotNull: Boolean,
    val defaultValue: ValueType?
) {

    /**
     * Complete name of the column using its table's name.
     */
    val fullName = "$tableName.$name"

    /**
     * Default alias of the column used in reading.
     */
    val alias = tableName + SEPARATOR + name

    /**
     * Builder used to create a column in a table.
     *
     * @param tableName name of the table that contains this column.
     * @param name name of the column.
     * @param type type of the column (one of [Type]).
     */
    open class Builder<ValueType>(
        private val tableName: String,
        private val name: String,
        private val type: Type
    ) {

        private var isPrimaryKey: Boolean = false
        private var isUnique: Boolean = false
        private var isNotNull: Boolean = false
        private var defaultValue: ValueType? = null

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

        /**
         * Creates an instance of [Column] using the parameters of this [Builder].
         * This method will additionally check if the parameters set on this [Builder] are valid.
         *
         * @return instance of [Column].
         */
        fun build(): Column<ValueType> {
            if (tableName.contains(SEPARATOR) || name.contains(SEPARATOR))
                throw IllegalArgumentException("The character \"$SEPARATOR\" isn't allowed on column's or table's name.")

            return Column(
                tableName,
                name,
                type,
                isPrimaryKey,
                isUnique,
                isNotNull,
                defaultValue
            )
        }
    }

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
class RealColumnBuilder(table: String, name: String) :
    Column.Builder<Double>(table, name, Column.Type.REAL)

/**
 * Builder used to create an integer or a boolean column in a table.
 */
class IntegerColumnBuilder(table: String, name: String) :
    Column.Builder<Long>(table, name, Column.Type.INTEGER)

/**
 * Builder used to create a string column in a table.
 */
class TextColumnBuilder(table: String, name: String) :
    Column.Builder<String>(table, name, Column.Type.TEXT)

/**
 * Builder used to create a byte array column in a table.
 */
class BlobColumnBuilder(table: String, name: String) :
    Column.Builder<ByteArray>(table, name, Column.Type.BLOB)