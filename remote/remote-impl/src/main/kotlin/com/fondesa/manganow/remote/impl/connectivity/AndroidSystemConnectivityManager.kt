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

package com.fondesa.manganow.remote.impl.connectivity

import android.content.Context
import com.fondesa.manganow.remote.api.connectivity.ConnectivityManager
import dagger.Reusable
import javax.inject.Inject
import android.net.ConnectivityManager as AndroidConnectivityManager

/**
 * Implementation of [ConnectivityManager] which uses the system service of Android to check
 * the connectivity's status.
 *
 * @param context the [Context] used to access to the system services.
 */
@Reusable
class AndroidSystemConnectivityManager @Inject constructor(private val context: Context) :
    ConnectivityManager {

    private val connectivityManager by lazy {
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as AndroidConnectivityManager
    }

    override fun isConnected(): Boolean {
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo?.isConnectedOrConnecting ?: false
    }
}