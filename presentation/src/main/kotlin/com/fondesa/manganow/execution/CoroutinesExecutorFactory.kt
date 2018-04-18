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

package com.fondesa.manganow.execution

import javax.inject.Inject
import kotlin.coroutines.experimental.CoroutineContext

/**
 * Factory used to create a [CoroutinesExecutor.Builder].
 *
 * @param uiContext [CoroutineContext] used to invoke the callbacks on the UI thread.
 */
class CoroutinesExecutorFactory @Inject constructor(private val uiContext: CoroutineContext) :
    ExecutorFactory {

    override fun <T> create(executionBlock: ExecutionBlock<T>): Executor.Builder<T> =
        CoroutinesExecutor.Builder(uiContext, executionBlock)
}