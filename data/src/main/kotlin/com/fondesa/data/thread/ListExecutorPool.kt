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

import javax.inject.Inject

/**
 * Implementation of [ExecutorPool] which uses a [List] to handle the pool.
 */
class ListExecutorPool @Inject constructor() : ExecutorPool {

    private val executors = mutableListOf<Executor>()

    override fun add(executor: Executor) {
        executors.add(executor)
    }

    override fun release() {
        executors.forEach { it.cancel() }
        executors.clear()
    }
}