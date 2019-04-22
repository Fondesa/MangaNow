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

package com.fondesa.manganow.core.api.coroutines

import kotlinx.coroutines.*

/**
 * Used to execute an asynchronous operation awaiting for its result.
 *
 * @param dispatcher the context of the coroutine. By default it's [CommonPool].
 * @param start the [CoroutineStart] type. By default it's [CoroutineStart.DEFAULT].
 * @param block the asynchronous operation which must be executed.
 * @param T the returning type of the suspending function.
 * @return the result of the asynchronous operation.
 */
suspend fun <T> asyncAwait(
    dispatcher: CoroutineDispatcher = Dispatchers.Default,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> T
): T = coroutineScope { async(dispatcher, start, block = block).await() }

/**
 * Executes a suspending function catching all the [Throwable] thrown by its execution.
 * The optional result can be received through [TryingHandler.onSuccess].
 * The optional [Throwable] can be received through [TryingHandler.onError].
 *
 * @param dispatchOnlyWhenActive true if the callbacks should be invoked only if the [Job]
 * wasn't cancelled before. By default true.
 * @param block the suspending function which should be executed.
 * @param T the returning type of the suspending function.
 * @return instance of [TryingHandler] used to manage the callbacks.
 */
suspend fun <T> CoroutineScope.trying(
    dispatchOnlyWhenActive: Boolean = true,
    block: suspend () -> T
): TryingHandler<T> {

    val shouldDispatch = {
        !dispatchOnlyWhenActive || isActive
    }

    if (!shouldDispatch()) {
        return TryingHandler.Inactive()
    }

    return try {
        // Execute the suspend returning function.
        val result = block()
        if (!shouldDispatch()) {
            TryingHandler.Inactive()
        } else {
            TryingHandler.Success(result)
        }
    } catch (t: Throwable) {
        if (!shouldDispatch()) {
            TryingHandler.Inactive()
        } else {
            TryingHandler.Error(t)
        }
    }
}