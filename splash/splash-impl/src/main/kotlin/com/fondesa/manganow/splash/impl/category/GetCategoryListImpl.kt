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

package com.fondesa.manganow.splash.impl.category

import com.fondesa.manganow.splash.impl.category.storage.disk.CategoryDiskStorageFactory
import com.fondesa.manganow.splash.impl.category.storage.remote.CategoryRemoteStorageFactory
import javax.inject.Inject

// TODO USE PROVIDERS
class GetCategoryListImpl @Inject constructor(
    private val remoteStorageFactory: CategoryRemoteStorageFactory,
    private val diskStorageFactory: CategoryDiskStorageFactory
) : GetCategoryList {

    override suspend fun execute(): CategoryList {
        val diskStorage = diskStorageFactory.provideStorage()
        return if (diskStorage.isValid()) {
            diskStorage.get()
        } else {
            val remoteStorage = remoteStorageFactory.provideStorage()
            remoteStorage.get().also {
                diskStorage.put(it)
            }
        }
    }
}



