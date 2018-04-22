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

package com.fondesa.data.latest.repository

import com.fondesa.data.cache.Cache
import com.fondesa.data.remote.RemoteApi
import com.fondesa.data.remote.loader.RemoteLoader
import com.fondesa.domain.latest.LatestList
import com.fondesa.domain.latest.repository.LatestRepository
import javax.inject.Inject

class DefaultLatestRepository @Inject constructor(
    private val remoteLoader: @JvmSuppressWildcards RemoteLoader<LatestList>,
    private val cache: @JvmSuppressWildcards Cache<LatestList>
) : LatestRepository {

    //TODO pass params to cache
    override suspend fun getPaginated(page: Int, pageSize: Int): LatestList = if (cache.isValid()) {
        cache.get()
    } else {
        val task = RemoteApi.Request.latest(page, pageSize)
        remoteLoader.load(task).also {
            cache.put(it)
        }
    }
}



