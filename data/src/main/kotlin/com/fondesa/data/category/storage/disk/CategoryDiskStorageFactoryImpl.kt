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

package com.fondesa.data.category.storage.disk

import com.fondesa.data.category.storage.CategoryDiskStorage
import com.fondesa.data.category.storage.CategoryDiskStorageFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class CategoryDiskStorageFactoryImpl @Inject constructor(private val client: com.fondesa.manganow.database.api.client.DatabaseClient) :
    CategoryDiskStorageFactory {

    override fun provideStorage(): CategoryDiskStorage {
        val expirationTimeMs = TimeUnit.DAYS.toMillis(7)
        val remoteTaskKey = "categories"
        return CategoryDiskStorageImpl(client, expirationTimeMs, remoteTaskKey)
    }
}