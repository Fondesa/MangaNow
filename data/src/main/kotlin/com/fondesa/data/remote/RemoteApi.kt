package com.fondesa.data.remote

import com.fondesa.remote.task.RemoteTask
import com.google.gson.JsonElement

/**
 * APIs for requests to MangaNow's webservice.
 */
object RemoteApi {

    /**
     * Keys used in the body of the requests or in query string.
     */
    object Key {
    }

    /**
     * Default values used in the body of the requests or in query string.
     */
    object Values {
    }

    /**
     * Constant paths appended to the base url.
     */
    object Path {
        const val CATEGORIES = "categories"
        const val SORT_ORDERS = "sortorders"
    }

    /**
     * Wrapper containing built-in requests to use through the application lifetime.
     */
    object Request {
        fun categories(): RemoteTask = Task.Get(Path.CATEGORIES)

        fun sortOrders(): RemoteTask = Task.Get(Path.SORT_ORDERS)
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
            private val apiPath: String,
            private val queryParams: Map<String, String> = emptyMap()
        ) : BaseTask() {

            override fun apiPath(): String = apiPath

            override fun method() = RemoteTask.Method.GET

            override fun queryParams() = queryParams

            override fun body(): JsonElement? = null

            override fun headers(): Map<String, String> = emptyMap()
        }

        data class ImageTask(private val imageUrl: String) : RemoteTask {

            override fun method() = RemoteTask.Method.GET

            override fun scheme() = "https"

            override fun host() = "cdn.mangaeden.com"

            override fun path() = "mangasimg/$imageUrl"

            override fun headers(): Map<String, String> = emptyMap()

            override fun queryParams(): Map<String, String> = emptyMap()

            override fun body(): JsonElement? = null
        }

        /**
         * Implementation of [RemoteTask] that defines the configurations in common for all tasks.
         */
        abstract class BaseTask : RemoteTask {

            override fun scheme() = "http"

            override fun host() = "192.168.1.2"

            override fun path() = "api/${apiPath()}"

            override fun port() = 8080

            /**
             * @return path that will be appended to the root path of the APIs.
             */
            abstract fun apiPath(): String
        }
    }
}