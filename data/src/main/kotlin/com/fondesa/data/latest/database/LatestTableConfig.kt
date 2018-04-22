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

package com.fondesa.data.latest.database

import com.fondesa.database.annotations.Column
import com.fondesa.database.annotations.Table
import com.fondesa.database.structure.IntegerColumnConfig

@Table("latest", withRowId = false)
object LatestTableConfig {

    @Column("cache_id")
    val COL_CACHE_ID = IntegerColumnConfig.primaryKey()

    @Column("manga_id")
    val COL_MANGA_ID = IntegerColumnConfig.primaryKey()

    @Column("chapter_id")
    val COL_CHAPTER_ID = IntegerColumnConfig.primaryKey()

//    @ForeignKey(RemoteTaskTableConfig::class)
//    val CACHE_ID_FK = ForeignKeyConfig.from("cache_id")
//        .to("id")
//        .onDelete(ForeignKeyAction.CASCADE)
//        .onUpdate(ForeignKeyAction.CASCADE)
//
//    @ForeignKey(MangaTableConfig::class)
//    val MANGA_ID_FK = ForeignKeyConfig.from("manga_id")
//        .to("id")
//        .onDelete(ForeignKeyAction.CASCADE)
//        .onUpdate(ForeignKeyAction.CASCADE)
//
//    @ForeignKey(ChapterTableConfig::class)
//    val CHAPTER_ID_FK = ForeignKeyConfig.from("chapter_id")
//        .to("id")
//        .onDelete(ForeignKeyAction.CASCADE)
//        .onUpdate(ForeignKeyAction.CASCADE)
}