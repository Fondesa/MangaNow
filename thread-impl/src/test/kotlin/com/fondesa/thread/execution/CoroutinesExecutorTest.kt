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

import com.fondesa.common.test.TestLock
import com.fondesa.common.thread.execution.ExecutionBlock
import com.fondesa.common.thread.execution.execute
import kotlinx.coroutines.experimental.delay
import org.junit.Test
import java.util.concurrent.TimeUnit
import kotlin.coroutines.experimental.EmptyCoroutineContext

/**
 * Tests for [CoroutinesExecutor].
 */
class CoroutinesExecutorTest {

    @Test
    fun executionCompletedSuccessfully() {
        val lock = TestLock()
        val number = 5

        createBuilder {
            number + number
        }.completed {
            assert(it == number + number)
            lock.unlock()
        }.execute()

        lock.lock()
    }

    @Test
    fun executionCompletedSuccessfullyAvoidErrorBlock() {
        val lock = TestLock(50, TimeUnit.MILLISECONDS)

        createBuilder {
            // Empty execution block.
        }.error {
            lock.unlock()
        }.execute()

        val errorBlockIsExecuted = lock.lock()
        assert(!errorBlockIsExecuted)
    }

    @Test
    fun executionCaughtException() {
        val lock = TestLock()

        val exc = RuntimeException()
        createBuilder {
            throw exc
        }.error { e ->
            assert(exc == e)
            lock.unlock()
        }.execute()

        lock.lock()
    }

    @Test
    fun executionCaughtExceptionAvoidCompletedBlock() {
        val lock = TestLock(50, TimeUnit.MILLISECONDS)

        createBuilder {
            throw RuntimeException()
        }.completed {
            lock.unlock()
        }.execute()

        val completedBlockIsExecuted = lock.lock()
        assert(!completedBlockIsExecuted)
    }

    @Test
    fun executorManagesLoading() {
        val startLoadingLock = TestLock()
        val finishLoadingLock = TestLock()
        val completedLock = TestLock()

        val executor = createBuilder {
            startLoadingLock.unlock()
            // Wait till the lock will be unlocked.
            finishLoadingLock.lock()
        }.completed {
            completedLock.unlock()
        }.build()

        // At the start, the executor mustn't load anything.
        assert(!executor.isExecuting)
        // Load the task.
        executor.execute()
        // Wait till the loading is sent to a background thread.
        startLoadingLock.lock()
        // The background thread is processing now, so the executor must be loading.
        assert(executor.isExecuting)
        // Unlock the waiting on the background thread.
        finishLoadingLock.unlock()
        // Wait till the completed lock is executed.
        completedLock.lock()
        // The executor isn't loading anymore.
        assert(!executor.isExecuting)
    }

    @Test
    fun executorManagesLoadingOnCancellation() {
        val startLoadingLock = TestLock()
        val finishLoadingLock = TestLock()

        var isCallbackCalled = false

        val executor = createBuilder {
            startLoadingLock.unlock()
            // Wait till the lock will be unlocked.
            finishLoadingLock.lock()
        }.completed {
            isCallbackCalled = true
        }.execute()

        // Wait till the loading is sent to a background thread.
        startLoadingLock.lock()
        // The background thread is processing now, so the executor must be loading.
        assert(executor.isExecuting)
        // Cancel the task.
        executor.cancel()

        assert(executor.isCancelled)

        // Unlock the waiting on the background thread and finish the test.
        finishLoadingLock.unlock()

        // The executor mustn't call the callback.
        assert(!isCallbackCalled)
    }

    @Test
    fun executorSuspendsCancellation() {
        val startLoadingLock = TestLock()
        val finishLoadingLock = TestLock()

        var isCompletedCallbackCalled = false
        var isErrorCallbackCalled = false

        val executor = createBuilder {
            startLoadingLock.unlock()
            delay(1000)
            finishLoadingLock.lock()
        }.completed {
            isCompletedCallbackCalled = true
        }.error {
            isErrorCallbackCalled = true
        }.execute()

        startLoadingLock.lock()
        // The background thread is processing now, so the executor must be loading.
        assert(executor.isExecuting)
        // Cancel the executor.
        executor.cancel()

        assert(executor.isCancelled)

        finishLoadingLock.unlock()

        assert(!isCompletedCallbackCalled)
        assert(!isErrorCallbackCalled)

        // The executor mustn't load anything anymore.
        assert(!executor.isExecuting)
    }

    @Test
    fun taskTestComputationCancellation() {
        val startLoadingLock = TestLock()
        val finishLoadingLock = TestLock()

        var isCompletedCallbackCalled = false
        var isErrorCallbackCalled = false

        val executor = createBuilder {
            startLoadingLock.unlock()
            finishLoadingLock.lock()
        }.completed {
            isCompletedCallbackCalled = true
        }.error {
            isErrorCallbackCalled = true
        }.execute()

        startLoadingLock.lock()
        // The background thread is processing now, so the executor must be loading.
        assert(executor.isExecuting)
        // Cancel the executor.
        executor.cancel()

        assert(executor.isCancelled)

        finishLoadingLock.unlock()

        assert(!isCompletedCallbackCalled)
        assert(!isErrorCallbackCalled)

        // The executor mustn't load anything anymore.
        assert(!executor.isExecuting)
    }

    private fun <T> createBuilder(executionBlock: ExecutionBlock<T>) =
        CoroutinesExecutor.Builder(EmptyCoroutineContext, executionBlock)

    /**
     * Tests for [CoroutinesExecutor.Builder].
     */
    class BuilderTest {

        @Test
        fun buildValidExecutor() {
            val builder = CoroutinesExecutor.Builder(EmptyCoroutineContext) {
                /* Empty unit block. */
            }
            val executor = builder.build()
            assert(executor is CoroutinesExecutor<*>)
        }
    }
}