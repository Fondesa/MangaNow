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

import android.database.sqlite.SQLiteStatement
import com.fondesa.common.database.execution.Executor

/**
 * Implementation of [SQLiteBaseStatement] that will compile the raw string into a [SQLiteStatement]
 * used to execute operations as INSERT, UPDATE, DELETE, etc.. on database.
 *
 * @param raw SQLite statement written as string.
 */
abstract class SQLiteCompiledStatement<out E : Executor<*>>(val raw: String) :
    SQLiteBaseStatement<E>() {

    final override fun createExecutor(): E {
        logger.d("compiling: $raw")
        val statement = database.compileStatement(raw)
        return createExecutor(statement)
    }

    /**
     * Create the [Executor] that will execute this statement.
     *
     * @param statement compiled [SQLiteStatement].
     * @return instance of [Executor] that will execute this statement.
     */
    abstract fun createExecutor(statement: SQLiteStatement): E

    companion object {

        /**
         * Creates an anonymous subclass of [SQLiteCompiledStatement] specifying
         * the [Executor]'s creation from outside.
         *
         * @param raw SQLite statement written as string.
         * @param create block of code called to create the [Executor].
         * @return anonymous subclass of [SQLiteCompiledStatement].
         */
        inline fun <E : Executor<*>> lined(
            raw: String,
            crossinline create: (SQLiteStatement) -> E
        ) = object : SQLiteCompiledStatement<E>(raw) {
            override fun createExecutor(statement: SQLiteStatement) = create(statement)
        }
    }
}