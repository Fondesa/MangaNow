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

import com.fondesa.database.annotations.*
import com.fondesa.database.structure.Column.Type

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

        fun <T> fromSpec(tableName: String, spec: ColumnSpec<T>): Column<T> {
            val type = when(spec) {
                is RealColumnSpec -> Type.REAL
                is IntegerColumnSpec-> Type.INTEGER
                is TextColumnSpec -> Type.TEXT
                is BlobColumnSpec -> Type.BLOB
            }
            return Column(
                tableName,
                spec.name,
                type,
                spec.isPrimaryKey,
                spec.isUnique,
                spec.isNotNull,
                spec.defaultValue
            )
        }
    }
}