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

package com.fondesa.domain.manga.usecase

import com.fondesa.domain.manga.model.Manga
import com.fondesa.domain.manga.repository.MangaRepository
import com.fondesa.domain.usecase.UseCase
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.CoroutineScope
import kotlinx.coroutines.experimental.async
import javax.inject.Inject

class GetMangaList @Inject constructor(private val repository: MangaRepository) :
    UseCase<List<@JvmSuppressWildcards Manga>, Unit> {

    override suspend fun execute(params: Unit): List<Manga> =
        async(CommonPool) {
            repository.getList()
        }.await()
}

fun <T>asyncAwait(block: suspend CoroutineScope.() -> T) {
    async(CommonPool, block = block)
}