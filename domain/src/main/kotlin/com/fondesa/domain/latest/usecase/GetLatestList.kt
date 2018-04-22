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
import com.fondesa.domain.latest.repository.LatestRepository
import com.fondesa.domain.usecase.UseCase
import com.fondesa.thread.extension.asyncAwait
import javax.inject.Inject

class GetLatestList @Inject constructor(private val repository: LatestRepository) :
    UseCase<LatestList, GetLatestList.Params> {

    override suspend fun execute(params: GetLatestList.Params): LatestList = asyncAwait {
        repository.getPaginated(params.page, params.pageSize)
    }

    data class Params(
        val page: Int,
        val pageSize: Int
    )
}