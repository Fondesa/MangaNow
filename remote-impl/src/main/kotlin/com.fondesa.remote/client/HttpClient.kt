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

import com.fondesa.common.remote.client.RemoteClient
import com.fondesa.common.remote.connectivity.ConnectivityManager
import com.fondesa.common.remote.exception.ConnectivityException
import com.fondesa.common.remote.exception.ResponseException
import com.fondesa.common.remote.exception.TimeoutException
import com.fondesa.common.remote.task.RemoteTask
import com.fondesa.remote.BuildConfig
import com.google.gson.Gson
import com.google.gson.JsonElement
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Type of [RemoteClient] which uses an [OkHttpClient] to manage WS requests.
 *
 * @param gson instance of [Gson] used to serialize and deserialize a json.
 */
class HttpClient @Inject constructor(
    private val gson: Gson,
    private val connectivityManager: ConnectivityManager
) : RemoteClient {

    private val httpClient: OkHttpClient

    init {
        // Set the properties that will be used by OkHttpClient.
        val httpClientBuilder = OkHttpClient.Builder()
            .readTimeout(CONNECTION_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .connectTimeout(CONNECTION_TIMEOUT_SECONDS, TimeUnit.SECONDS)

        // Logging interceptor to log requests' descriptions.
        val logInterceptor = HttpLoggingInterceptor()
        logInterceptor.level = HttpLoggingInterceptor.Level.BODY
        httpClientBuilder.addInterceptor(logInterceptor)

        // Build the OkHttpClient.
        httpClient = httpClientBuilder.build()
    }

    override fun load(task: RemoteTask): JsonElement {
        // Check the connectivity before sending the request.
        if (!connectivityManager.isConnected())
            throw ConnectivityException()

        // Get the task URL.
        val url = task.toHttpUrl()

        val body = task.body?.let {
            // Content-type of the json.
            val mediaType = MediaType.parse("application/json; charset=utf-8")
            val jsonBody = gson.toJson(it)
            RequestBody.create(mediaType, jsonBody)
        }

        // Create the request.
        val requestBuilder = Request.Builder()
            .url(url)
            .headers(Headers.of(task.headers))
            .method(task.method.value, body)

        task.tag?.let {
            requestBuilder.tag(it)
        }

        val request = requestBuilder.build()

        val response = try {
            httpClient.newCall(request).execute()
        } catch (e: SocketTimeoutException) {
            throw TimeoutException()
        }

        val code = response.code()

        if (!response.isSuccessful) {
            response.close()
            throw ResponseException(
                code.toString(),
                "response not successful"
            )
        }

        val responseBody =
            response.body() ?: throw ResponseException(
                code.toString(),
                "response body is null"
            )

        // Parse the body as JsonElement.
        return gson.fromJson(responseBody.charStream(), JsonElement::class.java)
    }

    /**
     * Converts a [RemoteTask] to an OkHttp's [HttpUrl].
     *
     * @return instance of [HttpUrl] representing this [RemoteTask].
     */
    private fun RemoteTask.toHttpUrl(): HttpUrl {
        val urlBuilder = HttpUrl.Builder()
            .scheme(scheme)
            .host(host)
            .port(port)
            .addPathSegments(path)

        // Add all query string parameters.
        queryParams.forEach { (key, value) ->
            urlBuilder.addQueryParameter(key, value)
        }

        // Build the url.
        return urlBuilder.build()
    }

    companion object {
        private val CONNECTION_TIMEOUT_SECONDS = if (BuildConfig.DEBUG) 15L else 30L
    }
}