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

package com.fondesa.remote.impl.connectivity

import android.content.Context
import android.net.NetworkInfo
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Test
import android.net.ConnectivityManager as AndroidConnectivityManager

/**
 * Tests for [AndroidSystemConnectivityManager].
 */
class AndroidSystemConnectivityManagerTest {

    private val activeNetworkInfo = mock<NetworkInfo>()
    private val connectivityManager = mock<AndroidConnectivityManager> {
        on(it.activeNetworkInfo).thenReturn(activeNetworkInfo)
    }
    private val context = mock<Context> {
        on(it.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(connectivityManager)
    }
    private val manager =
        AndroidSystemConnectivityManager(context)

    @Test
    fun deviceConnectivityChanges() {
        whenever(activeNetworkInfo.isConnectedOrConnecting).thenReturn(true)

        assert(manager.isConnected())

        whenever(activeNetworkInfo.isConnectedOrConnecting).thenReturn(false)

        assert(!manager.isConnected())
    }
}