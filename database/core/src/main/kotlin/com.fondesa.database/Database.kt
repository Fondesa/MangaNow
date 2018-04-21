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

package com.fondesa.database

import com.fondesa.database.execution.Closeable
import com.fondesa.database.execution.Executor
import com.fondesa.database.statement.Statement

/**
 * Used to define the database behavior.
 */
interface Database {

    /**
     * Run a block of code in a database transaction to optimize some operations.
     *
     * @param tx block of code that must be executed inside the transaction.
     */
    fun transaction(tx: () -> Unit)

    /**
     * Compile a [Statement] and provides its [Executor].
     *
     * @param statement [Statement] that provides the [Executor].
     * @return [Executor] created with the compiled statement.
     */
    fun <E : Executor<*>> compile(statement: Statement<E>): E

    /**
     * Helper method used to finalize more than one [Executor].
     *
     * @param closeables [Closeable]s that must be closed.
     */
    fun finalize(vararg closeables: Closeable)
}

