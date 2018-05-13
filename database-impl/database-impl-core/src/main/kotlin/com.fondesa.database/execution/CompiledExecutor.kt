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
import com.fondesa.common.database.execution.Closeable
import com.fondesa.common.database.execution.Executor

/**
 * Type of [Executor] used to execute a [SQLiteStatement].
 * The [SQLiteStatement] must be passed from outside and it will be available during the execution.
 *
 * @property stmt [SQLiteStatement] that must be executed.
 */
abstract class CompiledExecutor<out T>(protected val stmt: SQLiteStatement) : Executor<T>,
    Closeable {

    /**
     * Executes the statement and return the execution's result.
     *
     * @param close true if the createExecutor must be closed after its execution.
     * @return result after the execution.
     */
    fun execute(close: Boolean): T {
        val result = execute()
        if (close)
            close()
        return result
    }

    override fun close() {
        // Close the statement after its execution.
        stmt.close()
    }
}