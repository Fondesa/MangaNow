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

import dagger.Reusable
import javax.inject.Inject

@Reusable
class GetLatestListImpl @Inject constructor(
    private val remoteStorageFactory: LatestRemoteStorageFactory,
    private val diskStorageFactory: LatestDiskStorageFactory
) : GetLatestList {

    override suspend fun execute(page: Int, pageSize: Int): LatestList {
        val diskStorage = diskStorageFactory.provideStorage(page, pageSize)
        return if (diskStorage.isValid()) {
            diskStorage.get()
        } else {
            val remoteStorage = remoteStorageFactory.provideStorage(page, pageSize)
            remoteStorage.get().also {
                diskStorage.put(it)
            }
        }
    }
}