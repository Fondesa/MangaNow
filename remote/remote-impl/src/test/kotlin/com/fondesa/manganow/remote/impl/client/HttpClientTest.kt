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

package com.fondesa.manganow.remote.impl.client

import com.fondesa.manganow.remote.api.connectivity.ConnectivityManager
import com.fondesa.manganow.remote.api.exception.ConnectivityException
import com.fondesa.manganow.remote.api.exception.ResponseException
import com.fondesa.manganow.remote.api.task.RemoteTask
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.nhaarman.mockitokotlin2.KStubbing
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.SocketPolicy
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import java.net.HttpURLConnection
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

/**
 * Tests for [HttpClient].
 */
class HttpClientTest {

    @get:Rule
    val expectedEx: ExpectedException = ExpectedException.none()

    private val connectivityManager =
        mock<ConnectivityManager> {
            // By default, simulate the connectivity on.
            on(it.isConnected()).thenReturn(true)
        }
    private val gson = Gson()
    private val client =
        HttpClient(gson, connectivityManager, 500L, TimeUnit.MILLISECONDS)

    private val server = MockWebServer()

    @After
    fun shutDownServer() {
        server.shutdown()
    }

    @Test
    fun taskMappedToRequest() {
        // Return an empty json body.
        server.enqueue(MockResponse().setBody(JsonObject().toString()))
        server.start()

        val taskMethod = RemoteTask.Method.POST
        val taskPath = "dummy-path"
        val taskHeaders = mapOf("dummy-header-key" to "dummy-header-value")
        val taskQueryParams = mapOf("dummy-qp-key" to "dummy-qp-value")
        val taskBody = JsonObject().apply {
            addProperty("dummy-property", "dummy-value")
        }

        val task = createTask {
            on(it.method).thenReturn(taskMethod)
            on(it.path).thenReturn(taskPath)
            on(it.headers).thenReturn(taskHeaders)
            on(it.queryParams).thenReturn(taskQueryParams)
            on(it.body).thenReturn(taskBody)
        }

        client.load(task)

        val request = server.takeRequest()
        val requestUrl = request.requestUrl

        assert(requestUrl.pathSegments().last() == taskPath)
        assert(request.method == taskMethod.value)

        val headersMatch = taskHeaders.keys.all {
            taskHeaders[it] == request.getHeader(it)
        }
        assert(headersMatch)

        val queryParamsMatch = requestUrl.queryParameterNames().all {
            taskQueryParams[it] == requestUrl.queryParameter(it)
        }
        assert(queryParamsMatch)

        assert(request.body.readUtf8() == taskBody.toString())
    }

    @Test
    fun noConnectivityThrowsException() {
        whenever(connectivityManager.isConnected()).thenReturn(false)
        expectedEx.expect(ConnectivityException::class.java)
        val task = createTask()
        client.load(task)
    }

    @Test
    fun timeoutOnResponse() {
        server.enqueue(MockResponse().setSocketPolicy(SocketPolicy.NO_RESPONSE))
        server.start()

        expectedEx.expect(TimeoutException::class.java)
        val task = createTask()

        client.load(task)
    }

    @Test
    fun failedForStatusCode() {
        val statusCode = HttpURLConnection.HTTP_NOT_FOUND
        server.enqueue(MockResponse().setResponseCode(statusCode))
        server.start()

        expectedEx.expect(ResponseCodeMatcher(statusCode))

        val task = createTask()
        client.load(task)
    }

    @Test
    fun failedForInvalidBody() {
        val statusCode = HttpURLConnection.HTTP_OK
        server.enqueue(MockResponse().setResponseCode(statusCode))
        server.start()

        expectedEx.expect(ResponseCodeMatcher(statusCode))

        val task = createTask()
        client.load(task)
    }

    @Test
    fun successfulRequest() {
        val responseBody = JsonObject().apply {
            addProperty("dummy-prop-key", "dummy-prop-value")
        }
        server.enqueue(MockResponse().setBody(responseBody.toString()))
        server.start()

        val task = createTask()
        val result = client.load(task)

        assert(result == responseBody)
    }

    private inline fun createTask(block: KStubbing<RemoteTask>.(RemoteTask) -> Unit = {}) =
        mock<RemoteTask> {
            on(it.scheme).thenReturn("http")
            on(it.headers).thenReturn(emptyMap())
            on(it.method).thenReturn(RemoteTask.Method.GET)
            on(it.host).thenReturn(server.hostName)
            on(it.port).thenReturn(server.port)
            on(it.path).thenReturn("test-path")
            block(this, it)
        }

    private class ResponseCodeMatcher(private val code: Int) :
        TypeSafeMatcher<ResponseException>() {

        override fun matchesSafely(item: ResponseException): Boolean =
            item.code == code

        override fun describeTo(description: Description) {
            description.appendText("should match code ").appendText(code.toString())
        }
    }
}