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

package com.fondesa.remote.di

import com.fondesa.remote.api.client.RemoteClient
import com.fondesa.remote.api.connectivity.ConnectivityManager
import com.fondesa.remote.impl.client.HttpClient
import com.fondesa.remote.impl.connectivity.AndroidSystemConnectivityManager
import com.fondesa.remote.impl.injection.HttpClientInfo
import dagger.Binds
import dagger.Module
import dagger.Provides
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module(includes = [RemoteModule.WithProvides::class])
interface RemoteModule {

    @Binds
    fun provideConnectivityManager(manager: AndroidSystemConnectivityManager): ConnectivityManager

    @Singleton
    @Binds
    fun provideRemoteClient(client: HttpClient): RemoteClient

    @Module
    object WithProvides {

        @JvmStatic
        @HttpClientInfo
        @Provides
        fun provideTimeout(): Long = if (BuildConfig.DEBUG) 10L else 30L

        @JvmStatic
        @HttpClientInfo
        @Provides
        fun provideTimeoutUnit(): TimeUnit = TimeUnit.SECONDS
    }
}