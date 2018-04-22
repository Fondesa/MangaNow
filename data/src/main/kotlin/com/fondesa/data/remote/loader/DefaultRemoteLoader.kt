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

package com.fondesa.data.remote.loader

import com.fondesa.data.serialization.FromJsonConverter
import com.fondesa.remote.client.RemoteClient
import com.fondesa.remote.task.RemoteTask
import javax.inject.Inject

class DefaultRemoteLoader<out T> @Inject constructor(
    private val remoteClient: RemoteClient,
    private val converter: @JvmSuppressWildcards FromJsonConverter<T>
) : RemoteLoader<T> {

    override fun load(task: RemoteTask): T {
        val json = remoteClient.load(task)
        return converter.convert(json)
    }
}