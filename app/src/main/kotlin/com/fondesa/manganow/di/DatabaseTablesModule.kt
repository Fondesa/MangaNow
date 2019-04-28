/*
 * Copyright (c) 2019 Fondesa
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

package com.fondesa.manganow.di

import com.fondesa.manganow.domain.category.CategoryTableModule
import com.fondesa.manganow.domain.chapter.ChapterTableModule
import com.fondesa.manganow.domain.manga.MangaTableModule
import com.fondesa.manganow.domain.page.PageTableModule
import com.fondesa.manganow.latest.impl.LatestTableModule
import com.fondesa.manganow.mangalist.impl.MangaCacheTableModule
import com.fondesa.manganow.mangalist.impl.sortorder.SortOrderTableModule
import com.fondesa.manganow.storage.api.disk.CacheTableModule
import dagger.Module

@Module(
    includes = [
        CategoryTableModule::class,
        MangaTableModule::class,
        ChapterTableModule::class,
        PageTableModule::class,
        LatestTableModule::class,
        SortOrderTableModule::class,
        MangaCacheTableModule::class,
        CacheTableModule::class
    ]
)
interface DatabaseTablesModule