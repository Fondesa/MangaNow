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

package com.fondesa.manganow.mangalist.impl

import com.fondesa.manganow.database.api.client.DatabaseClient
import com.fondesa.manganow.database.api.client.clause.ConflictType
import com.fondesa.manganow.database.api.client.extension.equalTo
import com.fondesa.manganow.database.api.client.statement.Insert
import com.fondesa.manganow.database.api.client.statement.Select
import com.fondesa.manganow.database.api.client.statement.Update
import com.fondesa.manganow.domain.manga.Manga
import com.fondesa.manganow.domain.manga.MangaTable
import com.fondesa.manganow.storage.api.disk.SQLiteDiskStorage

class MangaListDiskStorage(
    client: DatabaseClient,
    cacheKey: String,
    expirationTimeMs: Long
) : SQLiteDiskStorage<MangaList>(client, cacheKey, expirationTimeMs) {

    override fun get(cacheId: Long): MangaList =
        database.compile(Statements.selectMangaCache(cacheId)).execute().map {
            // Read the information of the manga.
            Manga(
                id = it.getLong(MangaCacheTable.COL_MANGA_ID),
                alias = it.getString(MangaTable.COL_ALIAS),
                hits = it.getInt(MangaTable.COL_HITS),
                imageUrl = it.getNullableString(MangaTable.COL_IMAGE_URL),
                title = it.getString(MangaTable.COL_TITLE)
            )
        }

    override fun put(cacheId: Long, item: MangaList) {
        val mangaInsertExecutor = database.compile(Statements.insertManga())
        val mangaCacheInsertExecutor = database.compile(Statements.insertMangaCache())

        item.forEach { manga ->
            val mangaId = manga.id
            val mangaImageUrl = manga.imageUrl
            val mangaTitle = manga.title
            val mangaHits = manga.hits

            // Insert manga record if it wasn't in DB.
            val insertId = mangaInsertExecutor.bindLong(MangaTable.COL_ID, mangaId)
                .bindString(MangaTable.COL_ALIAS, manga.alias)
                .bindString(MangaTable.COL_TITLE, mangaTitle)
                .bindInt(MangaTable.COL_HITS, mangaHits)
                .bindString(MangaTable.COL_IMAGE_URL, mangaImageUrl)
                .execute()

            if (insertId == -1L) {
                // Update manga record if it was in DB,
                database.compile(Statements.updateManga(mangaId))
                    .bindString(MangaTable.COL_IMAGE_URL, mangaImageUrl)
                    .bindString(MangaTable.COL_TITLE, mangaTitle)
                    .bindInt(MangaTable.COL_HITS, mangaHits)
                    .execute()
            }

            // Insert manga cache record.
            mangaCacheInsertExecutor.bindLong(MangaCacheTable.COL_CACHE_ID, cacheId)
                .bindLong(MangaCacheTable.COL_MANGA_ID, mangaId)
                .execute()
        }
    }

    private object Statements {

        fun selectMangaCache(cacheId: Long) = Select.from(MangaCacheTable.NAME)
            .columns(
                MangaCacheTable.COL_CACHE_ID,
                MangaCacheTable.COL_MANGA_ID,
                MangaTable.COL_ALIAS,
                MangaTable.COL_HITS,
                MangaTable.COL_IMAGE_URL,
                MangaTable.COL_TITLE
            )
            .where(MangaCacheTable.COL_CACHE_ID.equalTo(cacheId))
            .innerJoin(MangaTable.NAME, MangaCacheTable.COL_MANGA_ID.equalTo(MangaTable.COL_ID))
            // TODO ORDER ASC OR DESC SORT ORDER
//            .orderAsc(MangaCacheTableConfig.COL_ID)
            .build()


        fun insertManga() = Insert.into(MangaTable.NAME)
            .conflictType(ConflictType.IGNORE)
            .columns(
                MangaTable.COL_ID,
                MangaTable.COL_ALIAS,
                MangaTable.COL_HITS,
                MangaTable.COL_IMAGE_URL,
                MangaTable.COL_TITLE
            )
            .build()

        fun updateManga(mangaId: Long) = Update.table(MangaTable.NAME)
            .conflictType(ConflictType.IGNORE)
            .columns(
                MangaTable.COL_IMAGE_URL,
                MangaTable.COL_TITLE,
                MangaTable.COL_HITS
            )
            .where(MangaTable.COL_ID.equalTo(mangaId))
            .build()

        fun insertMangaCache() = Insert.into(MangaCacheTable.NAME)
            .columns(
                MangaCacheTable.COL_CACHE_ID,
                MangaCacheTable.COL_MANGA_ID
            )
            .build()
    }
}