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

package com.fondesa.manganow.remote.api.task

import com.google.gson.JsonElement

data class RemoteImageTask(private val imageUrl: String) : RemoteTask {

    override val method = RemoteTask.Method.GET

    override val scheme = "https"

    override val host = "cdn.mangaeden.com"

    override val path = "mangasimg/$imageUrl"

    override val port: Int = 443

    override val headers: Map<String, String> = emptyMap()

    override val queryParams: Map<String, String> = emptyMap()

    override val body: JsonElement? = null
}