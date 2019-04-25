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

package com.fondesa.data.remote

import com.fondesa.manganow.remote.api.task.RemoteTask
import com.google.gson.JsonElement

/**
 * APIs for requests to MangaNow's webservice.
 */
object RemoteApi {

    /**
     * Keys used in the body of the requests or in query string.
     */
    object Key {
        const val PAGE = "page"
        const val PAGE_SIZE = "pageSize"
        const val SORT_ORDER_ID = "sortOrderId"
        const val TEXT_FILTER = "textFilter"
    }

    /**
     * Constant paths appended to the base url.
     */
    object Path {
        const val LATEST = "latest"
        const val MANGA_LIST = "mangalist"
    }

    /**
     * Wrapper containing built-in requests to use through the application lifetime.
     */
    object Request {
        fun latest(page: Int, pageSize: Int): RemoteTask {
            val queryParams = queryMap {
                put(Key.PAGE, page.toString())
                put(Key.PAGE_SIZE, pageSize.toString())
            }
            return Task.Get(Path.LATEST, queryParams)
        }

        private fun queryMap(values: MutableMap<String, String>.() -> Unit) =
            mutableMapOf<String, String>().apply(values)
    }

    /**
     * Common [RemoteTask]'s configurations used for requests.
     */
    object Task {

        /**
         * Implementation of [RemoteTask] for a GET request.
         *
         * @param apiPath the API path used for the request.
         * @param queryParams key-value params that will be added in query string.
         */
        data class Get(
            override val apiPath: String,
            override val queryParams: Map<String, String> = emptyMap()
        ) : BaseTask() {

            override val method = RemoteTask.Method.GET

            override val body: JsonElement? = null

            override val headers: Map<String, String> = emptyMap()
        }

        /**
         * Implementation of [RemoteTask] that defines the configurations in common for all tasks.
         */
        abstract class BaseTask : RemoteTask {

            /**
             * @return path that will be appended to the root path of the APIs.
             */
            protected abstract val apiPath: String

            override val scheme: String = "http"

            override val host: String = "192.168.1.7"

            override val path get() = "api/$apiPath"

            override val port = 8080
        }
    }
}