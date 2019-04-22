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

package com.fondesa.data.sortorder.storage.remote

import com.fondesa.data.remote.RemoteApi
import com.fondesa.data.sortorder.converter.SortOrderJsonConverter
import com.fondesa.data.sortorder.storage.SortOrderRemoteStorage
import com.fondesa.data.sortorder.storage.SortOrderRemoteStorageFactory
import com.fondesa.data.storage.remote.JsonRemoteStorage
import com.fondesa.manganow.remote.api.client.RemoteClient
import javax.inject.Inject

class SortOrderRemoteStorageFactoryImpl @Inject constructor(private val client: RemoteClient) :
    SortOrderRemoteStorageFactory {

    override fun provideStorage(): SortOrderRemoteStorage {
        val task = RemoteApi.Request.sortOrders()
        val jsonConverter = SortOrderJsonConverter()
        return JsonRemoteStorage(client, task, jsonConverter)
    }
}

