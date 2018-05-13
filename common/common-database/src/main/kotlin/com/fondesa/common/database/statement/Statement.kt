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

package com.fondesa.common.database.statement

import com.fondesa.common.database.Database
import com.fondesa.common.database.execution.Executor

/**
 * Defines a statement that could be executed through a [Executor].
 * The statement will be compiled inside the [Database].
 *
 * @param E the type of the [Executor] used to execute the compiled statement.
 */
interface Statement<out E : Executor<*>> {

    /**
     * Create the [Executor] that will execute this statement.
     *
     * @return instance of [Executor] that will execute this statement.
     */
    fun createExecutor(): E
}

