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

package com.fondesa.manganow.latest.impl

import com.fondesa.manganow.database.api.client.DatabaseClient
import com.fondesa.manganow.storage.api.disk.DiskStorage
import dagger.Reusable
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@Reusable
class LatestDiskStorageFactoryImpl @Inject constructor(private val client: DatabaseClient) :
    LatestDiskStorageFactory {

    override fun provideStorage(page: Int, pageSize: Int): DiskStorage<LatestList> {
        val cacheKey = "latest|p:$page|pS:$pageSize"
        val expirationTimeMs = TimeUnit.MINUTES.toMillis(5)
        return LatestDiskStorage(client, cacheKey, expirationTimeMs)
    }
}