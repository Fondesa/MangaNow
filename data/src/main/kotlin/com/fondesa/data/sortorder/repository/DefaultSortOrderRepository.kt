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

package com.fondesa.data.sortorder.repository

import com.fondesa.data.cache.Cache
import com.fondesa.data.remote.RemoteApi
import com.fondesa.data.remote.loader.RemoteLoader
import com.fondesa.domain.sortorder.SortOrderList
import com.fondesa.domain.sortorder.repository.SortOrderRepository
import javax.inject.Inject

class DefaultSortOrderRepository @Inject constructor(
    private val remoteLoader: @JvmSuppressWildcards RemoteLoader<SortOrderList>,
    private val cache: @JvmSuppressWildcards Cache<SortOrderList>
) : SortOrderRepository {

    override suspend fun getList(): SortOrderList = if (cache.isValid()) {
        cache.get()
    } else {
        val task = RemoteApi.Request.sortOrders()
        remoteLoader.load(task).also {
            cache.put(it)
        }
    }
}



