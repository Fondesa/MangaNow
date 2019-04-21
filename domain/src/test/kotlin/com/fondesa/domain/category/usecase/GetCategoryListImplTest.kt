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

import com.fondesa.common.test.runUnitBlocking
import com.fondesa.domain.category.model.Category
import com.fondesa.domain.category.repository.CategoryRepository
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Test

/**
 * Tests for [GetCategoryListImpl].
 */
class GetCategoryListImplTest {

    private val repository = mock<CategoryRepository>()
    private val useCase = GetCategoryListImpl(repository)

    @Test
    fun gotFromRepository() = runUnitBlocking {
        useCase.execute()
        verify(repository).getAll()
    }

    @Test
    fun obtainedSameRepositoryList() = runUnitBlocking {
        val dummyList = listOf(Category(3, "dummy-name"))
        whenever(repository.getAll()).thenReturn(dummyList)
        val resultList = useCase.execute()
        assert(resultList == dummyList)
    }
}