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

package com.fondesa.database.structure

/**
 * Builder used to create a column in a table.
 *
 * @param type type of the column (one of [Column.Type]).
 * @param ValueType type of the values contained in the column.
 */
sealed class ColumnConfig<ValueType>(val type: Column.Type) {
    internal var isPrimaryKey: Boolean = false
    internal var isUnique: Boolean = false
    internal var isNotNull: Boolean = false
    internal var defaultValue: ValueType? = null

    /**
     * Sets this column as primary key.
     * Automatically this column will be not null also if SQLite supports it for legacy builds.
     * A SQLite from can't use more than one primary key, so, if in the same [TableDefinition] there's more
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
}

/**
 * Builder used to create a floating-point column in a table.
 */
class RealColumnConfig : ColumnConfig<Double>(Column.Type.REAL)

/**
 * Builder used to create an integer or a boolean column in a table.
 */
class IntegerColumnConfig : ColumnConfig<Long>(Column.Type.INTEGER)

/**
 * Builder used to create a string column in a table.
 */
class TextColumnConfig : ColumnConfig<String>(Column.Type.TEXT)

/**
 * Builder used to create a byte array column in a table.
 */
class BlobColumnConfig : ColumnConfig<ByteArray>(Column.Type.BLOB)