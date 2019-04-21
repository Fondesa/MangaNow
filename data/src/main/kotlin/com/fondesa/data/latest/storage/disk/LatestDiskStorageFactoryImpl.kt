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

package com.fondesa.data.latest.storage.disk

import com.fondesa.data.latest.storage.LatestDiskStorage
import com.fondesa.data.latest.storage.LatestDiskStorageFactory
import com.fondesa.database.api.DatabaseClient
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class LatestDiskStorageFactoryImpl @Inject constructor(private val client: DatabaseClient) :
    LatestDiskStorageFactory {

    override fun provideStorage(page: Int, pageSize: Int): LatestDiskStorage {
        val expirationTimeMs = TimeUnit.MINUTES.toMillis(5)
        val remoteTaskPath = "latest-$page-$pageSize"
        return LatestDiskStorageImpl(client, expirationTimeMs, remoteTaskPath)
    }
}