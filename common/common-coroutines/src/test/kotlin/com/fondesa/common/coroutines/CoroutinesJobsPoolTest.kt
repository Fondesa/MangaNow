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

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.verifyNoMoreInteractions
import kotlinx.coroutines.experimental.Job
import org.junit.Test

/**
 * Tests for [CoroutinesJobsPool].
 */
class CoroutinesJobsPoolTest {

    private val pool = CoroutinesJobsPool()

    @Test
    fun releaseMultipleTimes() {
        val first = mock<Job>()
        pool.add(first)
        pool.cancelAll()

        verify(first).cancel()
        verifyNoMoreInteractions(first)

        val second = mock<Job>()
        pool.add(second)
        pool.cancelAll()

        verify(second).cancel()
    }

    @Test
    fun releaseTogether() {
        val first = mock<Job>()
        val second = mock<Job>()
        pool.add(first)
        pool.add(second)

        pool.cancelAll()

        verify(first).cancel()
        verify(second).cancel()
    }
}