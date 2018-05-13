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

import com.fondesa.common.database.execution.Executor
import timber.log.Timber

/**
 * Subclass of [SQLiteBaseStatement] that allows raw statements.
 *
 * @param raw SQLite statement written as string.
 */
abstract class SQLiteRawStatement<out E : Executor<*>>(val raw: String) : SQLiteBaseStatement<E>() {

    final override fun createExecutor(): E {
        Timber.d("compiling: $raw")
        return createExecutor(raw)
    }

    /**
     * Create the [Executor] that will execute this statement.
     *
     * @param raw SQLite statement written as string.
     * @return instance of [Executor] that will execute this statement.
     */
    abstract fun createExecutor(raw: String): E

    companion object {

        /**
         * Creates an anonymous subclass of [SQLiteRawStatement] specifying
         * the [Executor]'s creation from outside.
         *
         * @param raw SQLite statement written as string.
         * @param create block of code called to create the [Executor].
         * @return anonymous subclass of [SQLiteRawStatement].
         */
        inline fun <E : Executor<*>> lined(raw: String, crossinline create: (String) -> E) =
            object : SQLiteRawStatement<E>(raw) {
                override fun createExecutor(@Suppress("NAME_SHADOWING") raw: String) = create(raw)
            }
    }
}