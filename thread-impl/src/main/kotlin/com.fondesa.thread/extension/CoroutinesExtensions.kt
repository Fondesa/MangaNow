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

package com.fondesa.thread.extension

import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.CoroutineScope
import kotlinx.coroutines.experimental.CoroutineStart
import kotlinx.coroutines.experimental.async
import kotlin.coroutines.experimental.CoroutineContext

/**
 * Used to execute an asynchronous operation awaiting for its result.
 *
 * @param coroutineContext the context of the coroutine. By default it's [CommonPool].
 * @param start the [CoroutineStart] type. By default it's [CoroutineStart.DEFAULT].
 * @param block the asynchronous operation which must be executed.
 * @return the result of the asynchronous operation.
 */
suspend fun <T> asyncAwait(
    coroutineContext: CoroutineContext = CommonPool,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> T
): T = async(coroutineContext, start, block = block).await()