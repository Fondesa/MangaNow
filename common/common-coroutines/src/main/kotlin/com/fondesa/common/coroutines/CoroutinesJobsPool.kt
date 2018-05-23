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

package com.fondesa.common.coroutines

import kotlinx.coroutines.experimental.Job

/**
 * Used to manage a pool of coroutines' [Job]s and release them in a centralized way.
 */
class CoroutinesJobsPool {

    private val jobs = mutableListOf<Job>()

    /**
     * Adds an [Job] to the pool.
     *
     * @param job [Job] which must be added.
     */
    fun add(job: Job) {
        jobs.add(job)
    }

    /**
     * Cancel all [Job]s in the pool and release their references.
     */
    fun cancelAll() {
        jobs.forEach { it.cancel() }
        jobs.clear()
    }
}