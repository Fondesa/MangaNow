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
import org.junit.Test
import kotlin.coroutines.experimental.EmptyCoroutineContext

/**
 * Tests for [CoroutinesExecutorFactory].
 */
class CoroutinesExecutorFactoryTest {

    private val factory = CoroutinesExecutorFactory(uiContext = EmptyCoroutineContext)

    @Test
    fun createValidBuilder() {
        val completedLock = TestLock()
        val executionBlock: ExecutionBlock<Unit> = { /* Empty unit block. */ }
        val builder = factory.create(executionBlock)
            .completed {
                completedLock.unlock()
            }

        assert(builder is CoroutinesExecutor.Builder<Unit>)

        builder.execute()

        val executed = completedLock.lock()
        assert(executed)
    }
}