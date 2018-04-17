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

package com.fondesa.data.thread

/**
 * Used to create an [Executor] and execute it immediately.
 *
 * @param T the type of the operation's output.
 * @return the [Executor] built with the blocks added in this builder.
 */
fun <T> Executor.Builder<T>.execute(): Executor = build().apply { execute() }

/**
 * Used to create an [Executor] and execute it immediately adding it to a [ExecutorPool].
 *
 * @param pool the [ExecutorPool] in which the [Executor] must be added.
 * @param T the type of the operation's output.
 * @return the [Executor] built with the blocks added in this builder.
 */
fun <T> Executor.Builder<T>.execute(pool: ExecutorPool) {
    val executor = build()
    // Add it to the pool.
    pool.add(executor)
    // Execute it after adding it to the pool.
    executor.execute()
}