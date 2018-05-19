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

package com.fondesa.common.thread.execution

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import org.junit.Test

/**
 * Tests for ExecutorExtensions.kt file.
 */
class ExecutorExtensionsTest {

    @Test
    fun executeOnBuilder() {
        val executor = mock<Executor>()
        val builder = mock<Executor.Builder<*>> {
            on(it.build()).thenReturn(executor)
        }

        builder.execute()

        verify(builder).build()
        verify(executor).execute()
    }

    @Test
    fun executeWithPool() {
        val pool = mock<ExecutorPool>()
        val executor = mock<Executor>()
        val builder = mock<Executor.Builder<*>> {
            on(it.build()).thenReturn(executor)
        }

        builder.execute(pool)

        verify(builder).build()
        verify(pool).add(executor)
        verify(executor).execute()
    }
}