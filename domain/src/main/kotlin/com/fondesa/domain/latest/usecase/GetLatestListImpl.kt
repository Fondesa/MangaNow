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

package com.fondesa.domain.latest.usecase

import com.fondesa.domain.latest.LatestList
import com.fondesa.domain.latest.model.Latest
import com.fondesa.domain.latest.repository.LatestRepository
import com.fondesa.thread.extension.asyncAwait
import javax.inject.Inject

/**
 * Implementation of [GetLatestList] which uses a [LatestRepository] to obtain the
 * list of the [Latest] on another thread.
 *
 * @param repository the instance of [LatestRepository] used to get the list of all the [Latest].
 */
class GetLatestListImpl @Inject constructor(private val repository: LatestRepository) :
    GetLatestList {

    override suspend fun execute(page: Int, pageSize: Int): LatestList = asyncAwait {
        repository.getPaginated(page, pageSize)
    }
}