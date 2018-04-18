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

package com.fondesa.database.execution

import android.database.sqlite.SQLiteStatement
import com.fondesa.database.structure.Column

/**
 * Type of [BinderExecutor] that could bind the values of a [SQLiteStatement]
 * using also its columns' reference and execute it.
 * The binding of the values using the column reference will be slower than using the index.
 * A good strategy to maintain a good performance overall
 * would be preparing the indexes before the binding using [indexOf].
 *
 * @property stmt [SQLiteStatement] that can be bound or executed.
 * @property columns columns that could be bound.
 * @property executeBlock block that must be called to execute the [SQLiteStatement].
 */
class IndexedBinderExecutor<ExecutionType>(
    stmt: SQLiteStatement,
    private val columns: Array<out Column<*>>,
    executeBlock: (SQLiteStatement) -> ExecutionType
) : BinderExecutor<ExecutionType>(stmt, executeBlock) {

    /**
     * Bind a null value for a certain column.
     *
     * @param column column's name.
     */
    fun bindNull(column: Column<*>) = apply { bindNull(indexOf(column)) }

    /**
     * Bind a string value for a certain column.
     *
     * @param column column's name.
     * @param value value that must be bound or null.
     */
    fun bindString(column: Column<*>, value: String?) = apply { bindString(indexOf(column), value) }

    /**
     * Bind a long value for a certain column.
     *
     * @param column column's name.
     * @param value value that must be bound or null.
     */
    fun bindBoolean(column: Column<*>, value: Boolean?) = apply {
        bindBoolean(indexOf(column), value)
    }

    /**
     * Bind a long value for a certain column.
     *
     * @param column column's name.
     * @param value value that must be bound or null.
     */
    fun bindInt(column: Column<*>, value: Int?) = apply { bindLong(column, value?.toLong()) }

    /**
     * Bind a long value for a certain column.
     *
     * @param column column's name.
     * @param value value that must be bound or null.
     */
    fun bindLong(column: Column<*>, value: Long?) = apply { bindLong(indexOf(column), value) }

    /**
     * Bind a double value for a certain column.
     *
     * @param column column's name.
     * @param value value that must be bound or null.
     */
    fun bindDouble(column: Column<*>, value: Double?) = apply { bindDouble(indexOf(column), value) }

    /**
     * Bind a byte array value for a certain column.
     *
     * @param column column's name.
     * @param value value that must be bound or null.
     */
    fun bindBlob(column: Column<*>, value: ByteArray?) = apply { bindBlob(indexOf(column), value) }

    /**
     * Get the index of a column in the [SQLiteStatement].
     *
     * @param column column's reference.
     * @return index of the column inside the [SQLiteStatement].
     */
    fun indexOf(column: Column<*>): Int {
        val index = columns.indexOf(column)
        if (index < 0) {
            throw IllegalArgumentException("The column: ${column.name} is invalid.")
        }
        return index + 1
    }
}