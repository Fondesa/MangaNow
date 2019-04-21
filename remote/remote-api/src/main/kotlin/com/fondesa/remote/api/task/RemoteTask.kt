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

package com.fondesa.remote.api.task

import com.fondesa.remote.api.client.RemoteClient
import com.google.gson.JsonElement

/**
 * Definition of a request that should be called by the [RemoteClient].
 * It's recommended to provide an implementation for GET/POST/PUT/DELETE requests to avoid to
 * repeat the code related to a task.
 */
interface RemoteTask {

    /**
     * Specifies the method of the request.
     * Must be one of [RemoteTask.Method].
     *
     * @return method of the request.
     */
    val method: Method

    /**
     * Specifies the scheme of the request.
     * E.g. http, https, tcp, telnet, etc..
     *
     * @return scheme of the request.
     */
    val scheme: String

    /**
     * Specifies the host of the request.
     * It could be a named host as www.google.it or an IP.
     *
     * @return host of the request.
     */
    val host: String

    /**
     * Specifies the port number of the request.
     *
     * @return port number of the request.
     */
    val port: Int

    /**
     * Specifies the path of the request that will be appended to the absolute path.
     * The absolute path is created with [scheme]://[host]:[port]/
     *
     * @return path of the request.
     */
    val path: String

    /**
     * Specifies the headers that will be added to the request.
     *
     * @return key-value map representing the headers.
     */
    val headers: Map<String, String>

    /**
     * Specifies the query parameters that will be added to the request.
     *
     * @return key-value map representing the query parameters.
     */
    val queryParams: Map<String, String>

    /**
     * Specifies the body of the request.
     *
     * @return [JsonElement] representing the body of the request or null.
     */
    val body: JsonElement?

    /**
     * Specifies the tag of the request.
     * The tag is used to recognize the request after its creation.
     * By default the request doesn't use a tag.
     *
     * @return tag of the request.
     */
    val tag: Any? get() = null

    /**
     * List of request's available HTTP methods.
     *
     * @param value the HTTP method that should be used.
     */
    enum class Method(val value: String) {

        /**
         * Specifies a GET method.
         */
        GET("GET"),

        /**
         * Specifies a POST method.
         */
        POST("POST"),

        /**
         * Specifies a PUT method.
         */
        PUT("PUT"),

        /**
         * Specifies a DELETE method.
         */
        DELETE("DELETE"),

        /**
         * Specifies a PATCH method.
         */
        PATCH("PATCH")
    }
}