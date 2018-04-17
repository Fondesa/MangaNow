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

package com.fondesa.data.sortorder.store

import com.fondesa.data.store.CacheDataStore
import com.fondesa.domain.sortorder.model.SortOrder
import javax.inject.Inject

class SortOrderCacheDataStore @Inject constructor() :
    CacheDataStore<List<@JvmSuppressWildcards SortOrder>> {

    override suspend fun get(): List<SortOrder> {
        TODO("not implemented")
    }

    override suspend fun save(mangaList: List<SortOrder>) {
        TODO("not implemented")
    }

    override suspend fun isValid(): Boolean {
        TODO("not implemented")
    }
}