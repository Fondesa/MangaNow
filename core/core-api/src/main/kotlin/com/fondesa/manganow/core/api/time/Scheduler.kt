/*
 * Copyright (c) 2019 Fondesa
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

package com.fondesa.manganow.core.api.time

/**
 * Used to schedule a task which will be executed after a certain period of time.
 * The [Scheduler] is only a layer to avoid the direct usage of Android time API.
 */
interface Scheduler {

    /**
     * Schedules a task which will be executed after a certain period of time.
     *
     * @param time amount of time after which the task must be executed.
     * @param block task that must be executed.
     */
    fun schedule(time: Long, block: () -> Unit)

    /**
     * Cancel the scheduling of the task.
     */
    fun cancel()

    /**
     * Releases any resource related to the task and cancel the scheduling.
     */
    fun release()
}