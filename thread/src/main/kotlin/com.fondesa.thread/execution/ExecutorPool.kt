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

package com.fondesa.thread.execution

/**
 * Used to manage a pool of [Executor] and release them in a centralized way.
 */
interface ExecutorPool {

    /**
     * Adds an [Executor] to the pool.
     *
     * @param executor [Executor] which must be added.
     */
    fun add(executor: Executor)

    /**
     * Cancel all [Executor]s in the pool and release their references.
     */
    fun release()
}