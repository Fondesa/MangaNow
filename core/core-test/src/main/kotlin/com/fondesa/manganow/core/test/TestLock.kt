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

package com.fondesa.manganow.core.test

import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

/**
 * Used to test multi-threading operations locking the thread in which the test is currently running.
 *
 * @param timeout the default timeout for all the locks.
 * @param timeUnit the timeout's [TimeUnit] for all the locks.
 */
class TestLock(private val timeout: Long = 10, private val timeUnit: TimeUnit = TimeUnit.SECONDS) {

    private val latch = CountDownLatch(1)

    /**
     * Locks the current thread till [unlock] is called.
     *
     * @param timeout the lock's timeout.
     * @param timeUnit the timeout's [TimeUnit].
     * @return true if the elapsed time didn't exceed the [timeout].
     */
    fun lock(timeout: Long = this.timeout, timeUnit: TimeUnit = this.timeUnit): Boolean =
        latch.await(timeout, timeUnit)

    /**
     * Unlocks the current thread.
     */
    fun unlock() {
        latch.countDown()
    }
}