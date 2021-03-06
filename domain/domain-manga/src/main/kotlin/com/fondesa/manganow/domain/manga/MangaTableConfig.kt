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

package com.fondesa.manganow.domain.manga

import com.fondesa.manganow.database.annotations.Column
import com.fondesa.manganow.database.annotations.Table
import com.fondesa.manganow.database.api.structure.IntegerColumnConfig
import com.fondesa.manganow.database.api.structure.TextColumnConfig

@Table("manga")
object MangaTableConfig {

    @Column("id")
    val COL_ID = IntegerColumnConfig().primaryKey()

    @Column("alias")
    val COL_ALIAS = TextColumnConfig().unique()

    @Column("author")
    val COL_AUTHOR = TextColumnConfig()

    @Column("description")
    val COL_DESCRIPTION = TextColumnConfig()

    @Column("hits")
    val COL_HITS = IntegerColumnConfig().notNull().defaultValue(0)

    @Column("image_url")
    val COL_IMAGE_URL = TextColumnConfig()

    @Column("year_of_release")
    val COL_YEAR_OF_RELEASE = IntegerColumnConfig()

    @Column("status")
    val COL_STATUS = IntegerColumnConfig().defaultValue(Manga.Status.UNKNOWN.value)

    @Column("title")
    val COL_TITLE = TextColumnConfig().notNull()

    @Column("is_favourite")
    val COL_IS_FAVOURITE = IntegerColumnConfig().notNull().defaultValue(0)
}