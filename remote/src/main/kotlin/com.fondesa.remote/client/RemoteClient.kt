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

package com.fondesa.remote.client

import com.fondesa.remote.task.RemoteTask
import com.google.gson.JsonElement

/**
 * Used to specify the behavior of the application to load an incoming task.
 */
interface RemoteClient {

    /**
     * Loads a [RemoteTask] calling the WS and converts its response to a [JsonElement].
     *
     * @param task task containing the request's configurations.
     * @return json returned from the WS.
     */
    fun load(task: RemoteTask): JsonElement
}