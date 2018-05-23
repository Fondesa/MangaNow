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

import com.fondesa.common.test.TestLock
import com.fondesa.common.test.runUnitBlocking
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.launch
import org.junit.Test

/**
 * Tests for CoroutinesExtensions.kt file.
 */
class CoroutinesExtensionsTest {

    @Test
    fun asyncAwaitReturnsSuspendingResult() = runUnitBlocking {
        val result = asyncAwait { 1 + 1 }
        assert(result == 2)
    }

    @Test
    fun tryingSuccessfulResultReturnsSuccessHandler() = runUnitBlocking {
        val expectedResult = "result"
        val handler = trying {
            expectedResult
        }

        assert((handler as? TryingHandler.Success)?.result == expectedResult)
    }

    @Test
    fun tryingExceptionResultReturnsErrorHandler() = runUnitBlocking {
        val expectedThrowable = Throwable()
        val handler = trying {
            throw expectedThrowable
        }

        assert((handler as? TryingHandler.Error)?.throwable == expectedThrowable)
    }

    @Test
    fun tryingSuccessfulResultAfterCancellationReturnsSuccessHandlerWhenShouldBeDispatched() =
        runUnitBlocking {
            val expectedResult = "result"
            val startLock = TestLock()
            val endLock = TestLock()

            lateinit var handler : TryingHandler<String>

            val deferredResult = launch {
                handler = trying(dispatchOnlyWhenActive = false) {
                    startLock.unlock()
                    Thread.sleep(COROUTINES_WORKER_TIME_MS)
                    expectedResult
                }
                endLock.unlock()
            }

            startLock.lock()
            deferredResult.cancel()
            endLock.lock()
            assert((handler as? TryingHandler.Success)?.result == expectedResult)
        }

    @Test
    fun tryingErrorResultAfterCancellationReturnsErrorHandlerWhenShouldBeDispatched() =
        runUnitBlocking {
            val expectedThrowable = Throwable()
            val startLock = TestLock()
            val endLock = TestLock()

            lateinit var handler : TryingHandler<String>

            val deferredThrowable = launch {
                handler = trying(dispatchOnlyWhenActive = false) {
                    startLock.unlock()
                    Thread.sleep(COROUTINES_WORKER_TIME_MS)
                    throw expectedThrowable
                }
                endLock.unlock()
            }

            startLock.lock()
            deferredThrowable.cancel()
            endLock.lock()
            assert((handler as? TryingHandler.Error)?.throwable == expectedThrowable)
        }

    @Test
    fun tryingSuccessfulResultAfterCancellationReturnsInactiveHandler() =
        runUnitBlocking {
            val expectedResult = "result"
            val startLock = TestLock()
            val endLock = TestLock()

            lateinit var handler : TryingHandler<String>

            val deferredResult = launch {
                handler = trying {
                    startLock.unlock()
                    Thread.sleep(COROUTINES_WORKER_TIME_MS)
                    expectedResult
                }
                endLock.unlock()
            }

            startLock.lock()
            deferredResult.cancel()
            endLock.lock()
            assert(handler is TryingHandler.Inactive)
        }

    @Test
    fun tryingErrorResultAfterCancellationReturnsInactiveHandlerWhenShouldBeDispatched() =
        runUnitBlocking {
            val expectedThrowable = Throwable()
            val startLock = TestLock()
            val endLock = TestLock()

            lateinit var handler : TryingHandler<String>

            val deferredThrowable = launch {
                handler = trying {
                    startLock.unlock()
                    Thread.sleep(COROUTINES_WORKER_TIME_MS)
                    throw expectedThrowable
                }
                endLock.unlock()
            }

            startLock.lock()
            deferredThrowable.cancel()
            endLock.lock()
            assert(handler is TryingHandler.Inactive)
        }

    @Test
    fun jobAddedInPool() {
        val job = Job()
        val pool = mock<CoroutinesJobsPool>()
        job.inPool(pool)

        verify(pool).add(job)
    }

    companion object {
        private const val COROUTINES_WORKER_TIME_MS = 50L
    }
}