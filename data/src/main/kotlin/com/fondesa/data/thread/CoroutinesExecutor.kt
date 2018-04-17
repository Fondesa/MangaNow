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

import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.launch
import kotlin.coroutines.experimental.CoroutineContext

/**
 * Implementation of [Executor] which uses Kotlin's coroutines.
 *
 * @param uiContext [CoroutineContext] used to invoke the callbacks on the UI thread.
 * @param executionBlock [ExecutionBlock] obtained from an [Executor.Builder].
 * @param completedBlock optional [CompletedBlock] obtained from an [Executor.Builder].
 * @param errorBlock optional [ErrorBlock] obtained from an [Executor.Builder].
 * @param T the type of the operation's output which will be returned to the optional [CompletedBlock].
 */
class CoroutinesExecutor<T>(
    private val uiContext: CoroutineContext,
    private val executionBlock: ExecutionBlock<T>,
    private val completedBlock: CompletedBlock<T>?,
    private val errorBlock: ErrorBlock?
) : Executor {

    override val isExecuting: Boolean get() = job?.isActive == true

    override val isCancelled: Boolean get() = job?.isCancelled == true

    private var job: Job? = null

    override fun execute() {
        job = launch(uiContext) {
            val result = try {
                executionBlock()
            } catch (t: Throwable) {
                if (!isCancelled) {
                    // If the task wasn't cancelled (e.g. with a CancellationException), execute the error block.
                    errorBlock?.invoke(t)
                }
                return@launch
            }

            if (isCancelled)
                return@launch

            // Execute the completed block only if the task wasn't cancelled.
            completedBlock?.invoke(result)
        }
    }

    override fun cancel() {
        job?.cancel()
    }

    /**
     * Used to build a [CoroutinesExecutor].
     *
     * @param uiContext [CoroutineContext] used to invoke the callbacks on the UI thread.
     * @param executionBlock block used to execute the operation.
     * @param T the type of the operation's output which will be returned to the optional [CompletedBlock].
     */
    class Builder<T>(
        private val uiContext: CoroutineContext,
        executionBlock: ExecutionBlock<T>
    ) : Executor.Builder<T>(executionBlock) {

        override fun build(): Executor =
            CoroutinesExecutor(uiContext, executionBlock, completedBlock, errorBlock)
    }
}