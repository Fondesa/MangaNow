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

package com.fondesa.domain.category.usecase

import com.fondesa.domain.category.CategoryList
import com.fondesa.domain.category.repository.CategoryRepository
import com.fondesa.manganow.core.coroutines.asyncAwait
import javax.inject.Inject

/**
 * Implementation of [GetCategoryList] which uses a [CategoryRepository] to obtain the
 * list of the categories on another thread.
 *
 * @param repository the instance of [CategoryRepository] used to get the list of all the categories.
 */
class GetCategoryListImpl @Inject constructor(private val repository: CategoryRepository) :
    GetCategoryList {

    override suspend fun execute(): CategoryList = asyncAwait {
        repository.getAll()
    }
}