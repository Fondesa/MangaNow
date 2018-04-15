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

package com.fondesa.data.feature.manga.repository

import com.fondesa.data.feature.manga.store.MangaCacheDataStore
import com.fondesa.data.feature.manga.store.MangaRemoteDataStore
import com.fondesa.domain.feature.manga.model.Manga
import com.fondesa.domain.feature.manga.repository.MangaRepository
import javax.inject.Inject

class DefaultMangaRepository @Inject constructor(
    private val remoteDataStore: MangaRemoteDataStore,
    private val cacheDataStore: MangaCacheDataStore
) : MangaRepository {

    override suspend fun getList(): List<Manga> = if (cacheDataStore.isValid()) {
        cacheDataStore.get()
    } else {
        remoteDataStore.get()
    }
}