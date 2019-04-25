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

abstract class RemoteBaseTask : RemoteTask {

    /**
     * @return path that will be appended to the root path of the APIs.
     */
    protected abstract val apiPath: String

    override val scheme: String = "http"

    override val host: String = "192.168.1.7"

    override val path get() = "api/$apiPath"

    override val port = 8080
}