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

package com.fondesa.core.coroutines

/**
 * Defines the way the callbacks of [trying] should be managed.
 *
 * @param T the type of a successful result.
 */
sealed class TryingHandler<T> {

    /**
     * Invokes [block] when the execution of the suspending function didn't throw any exception.
     *
     * @param block the lambda which must be invoked with the successful result.
     * @return same instance of [TryingHandler].
     */
    abstract fun onSuccess(block: ResultBlock<T>): TryingHandler<T>

    /**
     * Invokes [block] when the execution of the suspending function threw an exception.
     *
     * @param block the lambda which must be invoked with the exception.
     * @return same instance of [TryingHandler].
     */
    abstract fun onError(block: ErrorBlock): TryingHandler<T>

    /**
     * Type of [TryingHandler] used to manage the callbacks when the coroutine
     * is inactive and shouldn't be dispatched.
     */
    internal class Inactive<T> : TryingHandler<T>() {
        override fun onSuccess(block: ResultBlock<T>) = this
        override fun onError(block: ErrorBlock) = this
    }

    /**
     * Type of [TryingHandler] used to manage the callbacks when the coroutine
     * threw an exception.
     *
     * @param throwable the caught [Throwable].
     */
    internal class Error<T>(internal val throwable: Throwable) : TryingHandler<T>() {
        override fun onSuccess(block: ResultBlock<T>) = this
        override fun onError(block: ErrorBlock) = apply { block(throwable) }
    }

    /**
     * Type of [TryingHandler] used to manage the callbacks when the coroutine
     * completed successfully.
     *
     * @param result the successful result.
     */
    internal class Success<T>(internal val result: T) : TryingHandler<T>() {
        override fun onSuccess(block: ResultBlock<T>) = apply { block(result) }
        override fun onError(block: ErrorBlock) = this
    }

}

private typealias ResultBlock<T> = (T) -> Unit

private typealias ErrorBlock = (Throwable) -> Unit