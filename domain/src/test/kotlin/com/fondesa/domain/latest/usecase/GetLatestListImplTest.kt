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

import com.fondesa.common.test.runUnitBlocking
import com.fondesa.domain.chapter.model.Chapter
import com.fondesa.domain.latest.model.Latest
import com.fondesa.domain.latest.repository.LatestRepository
import com.fondesa.domain.manga.model.Manga
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Test
import java.util.*

/**
 * Tests for [GetLatestListImpl].
 */
class GetLatestListImplTest {

    private val repository = mock<LatestRepository>()
    private val useCase = GetLatestListImpl(repository)

    @Test
    fun getsFromRepository() = runUnitBlocking {
        val page = 1
        val pageSize = 25
        useCase.execute(page, pageSize)
        verify(repository).getPaginated(page, pageSize)
    }

    @Test
    fun obtainedSameRepositoryList() = runUnitBlocking {
        val page = 1
        val pageSize = 25
        val dummyList = listOf(
            Latest(
                manga = Manga(
                    id = 4L,
                    alias = "dummy-alias",
                    imageUrl = "http://www.google.com",
                    title = "dummy-title"
                ),
                chapter = Chapter(
                    id = "dummy-id",
                    releaseDate = Date(1526746822797),
                    number = 34.5,
                    title = null
                )
            )
        )
        whenever(repository.getPaginated(page, pageSize)).thenReturn(dummyList)
        val resultList = useCase.execute(page, pageSize)
        assert(resultList == dummyList)
    }
}