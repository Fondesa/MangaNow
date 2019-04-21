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

package com.fondesa.database.api.execution

import android.database.sqlite.SQLiteStatement

/**
 * Type of [CompiledExecutor] that could bind the values of a [SQLiteStatement] and execute it.
 *
 * @property stmt [SQLiteStatement] that can be bound or executed.
 * @property executeBlock block that must be called to execute the [SQLiteStatement].
 */
open class BinderExecutor<ExecutionType>(
    stmt: SQLiteStatement,
    private val executeBlock: (SQLiteStatement) -> ExecutionType
) : CompiledExecutor<ExecutionType>(stmt) {

    /**
     * Bind a null value for a certain column's index.
     *
     * @param index column's index.
     */
    fun bindNull(index: Int) = apply { stmt.bindNull(index) }

    /**
     * Bind a string value for a certain column's index.
     *
     * @param index column's index.
     * @param value value that must be bound or null.
     */
    fun bindString(index: Int, value: String?) = apply {
        if (value == null)
            bindNull(index)
        else
            stmt.bindString(index, value)
    }

    /**
     * Bind a long value for a certain column's index.
     *
     * @param index column's index.
     * @param value value that must be bound or null.
     */
    fun bindBoolean(index: Int, value: Boolean?) = apply {
        val valueToInsert = when {
            value == null -> null
            value -> 1L
            else -> 0L
        }
        bindLong(index, valueToInsert)
    }

    /**
     * Bind a long value for a certain column's index.
     *
     * @param index column's index.
     * @param value value that must be bound or null.
     */
    fun bindInt(index: Int, value: Int?) = apply { bindLong(index, value?.toLong()) }

    /**
     * Bind a long value for a certain column's index.
     *
     * @param index column's index.
     * @param value value that must be bound or null.
     */
    fun bindLong(index: Int, value: Long?) = apply {
        if (value == null)
            bindNull(index)
        else
            stmt.bindLong(index, value)
    }

    /**
     * Bind a double value for a certain column's index.
     *
     * @param index column's index.
     * @param value value that must be bound or null.
     */
    fun bindDouble(index: Int, value: Double?) = apply {
        if (value == null)
            bindNull(index)
        else
            stmt.bindDouble(index, value)
    }

    /**
     * Bind a byte array value for a certain column's index.
     *
     * @param index column's index.
     * @param value value that must be bound or null.
     */
    fun bindBlob(index: Int, value: ByteArray?) = apply {
        if (value == null)
            bindNull(index)
        else
            stmt.bindBlob(index, value)
    }

    override fun execute(): ExecutionType = executeBlock(stmt)
}