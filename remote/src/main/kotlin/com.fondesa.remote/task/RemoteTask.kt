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

package com.fondesa.remote.task

import com.fondesa.remote.client.RemoteClient
import com.google.gson.JsonElement
import okhttp3.HttpUrl

/**
 * Definition of a request that must be called by the [RemoteClient].
 * <br/>
 * It's recommended to provide an implementation for GET/POST/PUT/DELETE requests to avoid to
 * repeat the code related to a task.
 */
interface RemoteTask {

    /**
     * Specify the method of the request.
     * Must be one of [RemoteTask.Method].
     *
     * @return method of the request.
     */
    fun method(): Method

    /**
     * Specify the scheme of the request.
     * E.g. http, https, tcp, telnet, etc..
     *
     * @return scheme of the request.
     */
    fun scheme(): String

    /**
     * Specify the host of the request.
     * It could be a named host as www.google.it or an IP.
     *
     * @return host of the request.
     */
    fun host(): String

    /**
     * Specify the port number of the request.
     * By default the port number is obtained from the [scheme].
     *
     * @return port number of the request.
     */
    fun port(): Int = HttpUrl.defaultPort(scheme())

    /**
     * Specify the path of the request that will be appended to the absolute path.
     * The absolute path is created with [scheme]://[host]:[port]/
     *
     * @return path of the request.
     */
    fun path(): String

    /**
     * Specify the headers that will be added to the request.
     *
     * @return key-value map representing the headers.
     */
    fun headers(): Map<String, String>

    /**
     * Specify the query parameters that will be added to the request.
     *
     * @return key-value map representing the query parameters.
     */
    fun queryParams(): Map<String, String>

    /**
     * Specify the body of the request.
     *
     * @return [JsonElement] representing the body of the request or null.
     */
    fun body(): JsonElement?

    /**
     * Specify the tag of the request.
     * The tag is used to recognize the request after its creation.
     * By default the request doesn't use a tag.
     *
     * @return tag of the request.
     */
    fun tag(): Any? = null

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