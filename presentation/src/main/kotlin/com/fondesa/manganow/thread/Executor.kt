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

package com.fondesa.manganow.thread

/**
 * Used to execute an operation.
 * The operation's execution can be checked through [isExecuting] and [isCancelled].
 */
interface Executor {

    /**
     * @return true if the operation is currently executing (started and not completed).
     */
    val isExecuting: Boolean

    /**
     * @return true if the operation's execution is cancelled.
     */
    val isCancelled: Boolean

    /**
     * Executes the operation.
     */
    fun execute()

    /**
     * Cancels the current operation which is executing, if any.
     */
    fun cancel()

    /**
     * Used to build an [Executor].
     *
     * @param executionBlock block used to execute the operation.
     * @param T the type of the operation's output which will be returned to the optional [CompletedBlock].
     */
    abstract class Builder<T>(protected val executionBlock: ExecutionBlock<T>) {
        protected var completedBlock: CompletedBlock<T>? = null
            private set
        protected var errorBlock: ErrorBlock? = null
            private set

        /**
         * Build an [Executor].
         *
         * @return the [Executor] built with the blocks added in this builder.
         */
        abstract fun build(): Executor

        /**
         * Define the block which is called when the [ExecutionBlock] is completed without errors.
         *
         * @param completedBlock block used to manage the result of [ExecutionBlock].
         * @return instance of this [Builder]
         */
        fun completed(completedBlock: CompletedBlock<T>) = apply {
            this.completedBlock = completedBlock
        }

        /**
         * Define the block which is called when an error was thrown during the
         * execution of the [ExecutionBlock].
         *
         * @param errorBlock block used to manage an error thrown by the [ExecutionBlock].
         * @return instance of this [Builder]
         */
        fun error(errorBlock: ErrorBlock): Builder<T> = apply {
            this.errorBlock = errorBlock
        }

        /**
         * Method used to create an [Executor] and execute it immediately.
         *
         * @return the [Executor] built with the blocks added in this builder.
         */
        fun load(): Executor = build().apply { execute() }
    }
}