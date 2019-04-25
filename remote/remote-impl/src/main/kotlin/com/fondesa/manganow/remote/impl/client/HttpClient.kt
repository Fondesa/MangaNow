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

package com.fondesa.manganow.remote.impl.client

import com.fondesa.manganow.remote.api.client.RemoteClient
import com.fondesa.manganow.remote.api.connectivity.ConnectivityManager
import com.fondesa.manganow.remote.api.exception.ConnectSocketException
import com.fondesa.manganow.remote.api.exception.ConnectivityException
import com.fondesa.manganow.remote.api.exception.ResponseException
import com.fondesa.manganow.remote.api.task.RemoteTask
import com.fondesa.manganow.remote.impl.qualifiers.HttpClientInfo
import com.google.gson.Gson
import com.google.gson.JsonElement
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException
import javax.inject.Inject

/**
 * Implementation of [RemoteClient] which uses an [OkHttpClient] to manage WS requests.
 *
 * @param gson instance of [Gson] used to serialize and deserialize a json.
 * @param connectivityManager instance of [ConnectivityManager] used to check
 * the connectivity's status.
 * @param timeout the timeout of the requests.
 * @param timeoutUnit the timeout's [TimeUnit] of the requests.
 */
class HttpClient @Inject constructor(
    private val gson: Gson,
    private val connectivityManager: ConnectivityManager,
    @HttpClientInfo timeout: Long,
    @HttpClientInfo timeoutUnit: TimeUnit
) : RemoteClient {

    private val httpClient: OkHttpClient

    init {
        // Set the properties that will be used by OkHttpClient.
        val httpClientBuilder = OkHttpClient.Builder()
            .readTimeout(timeout, timeoutUnit)
            .connectTimeout(timeout, timeoutUnit)

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
        } catch (e: ConnectException) {
            throw ConnectSocketException(url.host())
        }

        val code = response.code()

        if (!response.isSuccessful) {
            response.close()
            throw ResponseException(url.toString(), code, "response not successful")
        }

        return response.body()?.let {
            // Parse the body as a JsonElement.
            gson.fromJson(it.charStream(), JsonElement::class.java)
        } ?: throw ResponseException(url.toString(), code, "response body is null")
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
}