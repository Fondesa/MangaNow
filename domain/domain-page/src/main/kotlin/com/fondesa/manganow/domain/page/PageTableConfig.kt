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

package com.fondesa.manganow.domain.page

import com.fondesa.manganow.database.annotations.Column
import com.fondesa.manganow.database.annotations.ForeignKey
import com.fondesa.manganow.database.annotations.Table
import com.fondesa.manganow.database.api.structure.ForeignKeyAction
import com.fondesa.manganow.database.api.structure.ForeignKeyConfig
import com.fondesa.manganow.database.api.structure.IntegerColumnConfig
import com.fondesa.manganow.database.api.structure.TextColumnConfig
import com.fondesa.manganow.domain.chapter.ChapterTableConfig

@Table("page", withRowId = false)
object PageTableConfig {

    @Column("number")
    val COL_NUMBER = IntegerColumnConfig().primaryKey()

    @Column("image_url")
    val COL_IMAGE_URL = TextColumnConfig().notNull()

    @Column("chapter_id")
    val COL_CHAPTER_ID = IntegerColumnConfig().primaryKey().notNull()

    @ForeignKey(ChapterTableConfig::class)
    val MANGA_ID_FK = ForeignKeyConfig.from("chapter_id")
        .to("id")
        .onDelete(ForeignKeyAction.CASCADE)
        .onUpdate(ForeignKeyAction.CASCADE)
}