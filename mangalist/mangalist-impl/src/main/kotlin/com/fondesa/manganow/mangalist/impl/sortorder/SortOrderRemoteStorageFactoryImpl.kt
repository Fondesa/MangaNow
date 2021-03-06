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

package com.fondesa.manganow.mangalist.impl.sortorder

import com.fondesa.manganow.mangalist.api.sortorder.SortOrderList
import com.fondesa.manganow.remote.api.client.RemoteClient
import com.fondesa.manganow.remote.api.task.RemoteGetTask
import com.fondesa.manganow.storage.api.remote.JsonRemoteStorage
import com.fondesa.manganow.storage.api.remote.RemoteStorage
import com.fondesa.manganow.storage.api.remote.RemoteStorageMapper
import dagger.Reusable
import javax.inject.Inject

@Reusable
class SortOrderRemoteStorageFactoryImpl @Inject constructor(
    private val client: RemoteClient,
    private val mapper: RemoteStorageMapper<@JvmSuppressWildcards SortOrderList>
) : SortOrderRemoteStorageFactory {

    override fun provideStorage(): RemoteStorage<SortOrderList> {
        val task = RemoteGetTask(apiPath = "sortorders")
        return JsonRemoteStorage(client, task, mapper)
    }
}