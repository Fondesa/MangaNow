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

package com.fondesa.injection.remote

import com.fondesa.common.remote.client.RemoteClient
import com.fondesa.common.remote.connectivity.ConnectivityManager
import com.fondesa.remote.client.HttpClient
import com.fondesa.remote.connectivity.AndroidSystemConnectivityManager
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface RemoteModule {

    @Binds
    fun provideConnectivityManager(manager: AndroidSystemConnectivityManager): ConnectivityManager

    @Singleton
    @Binds
    fun provideRemoteClient(client: HttpClient): RemoteClient
}