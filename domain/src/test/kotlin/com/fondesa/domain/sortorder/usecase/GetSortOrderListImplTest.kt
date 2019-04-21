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

package com.fondesa.domain.sortorder.usecase

import com.fondesa.core.test.runUnitBlocking
import com.fondesa.domain.sortorder.model.SortOrder
import com.fondesa.domain.sortorder.repository.SortOrderRepository
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Test

/**
 * Tests for [GetSortOrderListImpl].
 */
class GetSortOrderListImplTest {

    private val repository = mock<SortOrderRepository>()
    private val useCase = GetSortOrderListImpl(repository)

    @Test
    fun gotFromRepository() = runUnitBlocking {
        useCase.execute()
        verify(repository).getAll()
    }

    @Test
    fun obtainedSameRepositoryList() = runUnitBlocking {
        val dummyList = listOf(SortOrder(3, "dummy-name", 4))
        whenever(repository.getAll()).thenReturn(dummyList)
        val resultList = useCase.execute()
        assert(resultList == dummyList)
    }
}