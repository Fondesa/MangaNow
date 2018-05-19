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

package com.fondesa.domain.chapter.model

import org.junit.Test
import java.util.*

/**
 * Tests for [Chapter].
 */
class ChapterTest {

    @Test
    fun areEquals() {
        assert(createChapter() == createChapter())
    }

    @Test
    fun sameHashCode() {
        assert(createChapter().hashCode() == createChapter().hashCode())
    }

    private fun createChapter() = Chapter(
        id = "dummy-id",
        releaseDate = Date(1526746822797),
        number = 34.5,
        title = null
    )
}