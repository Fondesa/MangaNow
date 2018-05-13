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

package com.fondesa.database.statement.base

import android.annotation.SuppressLint
import android.database.Cursor
import com.fondesa.common.database.execution.Executor
import timber.log.Timber

/**
 * Implementation of [SQLiteBaseStatement] that will compile the raw string into a [Cursor]
 * used to query the database.
 * The selection arguments are not supported in the raw query.
 *
 * @param raw SQLite statement written as string.
 * @param args the arguments that will be bound in the compiled statement.
 */
abstract class SQLiteQueryStatement<out E : Executor<*>>(
    val raw: String,
    val args: Array<out String>
) : SQLiteBaseStatement<E>() {

    @SuppressLint("Recycle")
    override fun createExecutor(): E {
        Timber.d("compiling: $raw")
        // Let the database to compile the query and bind the arguments.
        val cursor = database.rawQuery(raw, args)
        return createExecutor(cursor)
    }

    /**
     * Create the [Executor] that will execute this statement.
     *
     * @param cursor compiled [Cursor].
     * @return instance of [Executor] that will execute this statement.
     */
    abstract fun createExecutor(cursor: Cursor): E

    companion object {

        /**
         * Creates an anonymous subclass of [SQLiteQueryStatement] specifying
         * the [Executor]'s creation from outside.
         *
         * @param raw SQLite statement written as string.
         * @param create block of code called to create the [Executor].
         * @return anonymous subclass of [SQLiteQueryStatement].
         */
        inline fun <E : Executor<*>> lined(
            raw: String,
            args: Array<out String>,
            crossinline create: (Cursor) -> E
        ) = object : SQLiteQueryStatement<E>(raw, args) {
            override fun createExecutor(cursor: Cursor) = create(cursor)
        }
    }
}