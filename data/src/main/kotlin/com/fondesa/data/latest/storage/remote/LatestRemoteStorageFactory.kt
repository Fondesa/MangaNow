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

package com.fondesa.data.latest.storage.remote

import com.fondesa.data.latest.storage.LatestStorageFactory
import com.fondesa.data.storage.remote.RemoteStorage
import com.fondesa.domain.latest.LatestList

class LatestRemoteStorageFactory:
    LatestStorageFactory<RemoteStorage<LatestList>> {

    override fun provideStorage(page: Int, pageSize: Int): RemoteStorage<LatestList> {
        TODO("not implemented")
    }
}