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

package com.fondesa.data.latest.storage.disk

import com.fondesa.data.chapter.ChapterTable
import com.fondesa.data.latest.database.LatestTable
import com.fondesa.data.manga.database.MangaTable
import com.fondesa.data.storage.disk.SQLiteDiskStorage
import com.fondesa.database.api.DatabaseClient
import com.fondesa.database.clause.ConflictType
import com.fondesa.database.extension.equalTo
import com.fondesa.database.statement.Insert
import com.fondesa.database.statement.Select
import com.fondesa.database.statement.Update
import com.fondesa.domain.chapter.model.Chapter
import com.fondesa.domain.latest.LatestList
import com.fondesa.domain.latest.model.Latest
import com.fondesa.domain.manga.model.Manga
import java.util.*

class LatestDiskStorageImpl(
    client: DatabaseClient,
    expirationTimeMs: Long,
    remoteTaskKey: String
) : SQLiteDiskStorage<LatestList>(client, expirationTimeMs, remoteTaskKey) {

    override fun get(cacheId: Long): LatestList =
        database.compile(Statements.selectLatest(cacheId)).execute().map {
            // Read the information of the manga.
            val manga = Manga(
                id = it.getLong(LatestTable.COL_MANGA_ID),
                alias = it.getString(MangaTable.COL_ALIAS),
                imageUrl = it.getNullableString(MangaTable.COL_IMAGE_URL),
                title = it.getString(MangaTable.COL_TITLE)
            )

            // Read the information of the chapter.
            val chapter = Chapter(
                id = it.getString(LatestTable.COL_CHAPTER_ID),
                number = it.getDouble(ChapterTable.COL_NUMBER),
                releaseDate = Date(it.getLong(ChapterTable.COL_RELEASE_DATE)),
                title = it.getNullableString(ChapterTable.COL_TITLE)
            )

            Latest(manga, chapter)
        }

    override fun put(cacheId: Long, item: LatestList) {
        val mangaInsertExecutor = database.compile(Statements.insertManga())
        val chapterExecutor = database.compile(Statements.insertChapter())
        val latestCacheExecutor = database.compile(Statements.insertLatest())

        item.forEach {
            val manga = it.manga

            val mangaId = manga.id
            val mangaImageUrl = manga.imageUrl
            val mangaTitle = manga.title

            // Insert manga record if it wasn't in DB.
            val insertId = mangaInsertExecutor.bindLong(MangaTable.COL_ID, mangaId)
                .bindString(MangaTable.COL_ALIAS, manga.alias)
                .bindString(MangaTable.COL_TITLE, mangaTitle)
                .bindString(MangaTable.COL_IMAGE_URL, mangaImageUrl)
                .execute()

            if (insertId == -1L) {
                // Update manga record if it was in DB,
                database.compile(Statements.updateManga(mangaId))
                    .bindString(MangaTable.COL_IMAGE_URL, mangaImageUrl)
                    .bindString(MangaTable.COL_TITLE, mangaTitle)
                    .execute()
            }

            val chapter = it.chapter
            val chapterId = chapter.id

            // Insert chapter record.
            chapterExecutor.bindString(ChapterTable.COL_ID, chapterId)
                .bindDouble(ChapterTable.COL_NUMBER, chapter.number)
                .bindLong(ChapterTable.COL_RELEASE_DATE, chapter.releaseDate.time)
                .bindString(ChapterTable.COL_TITLE, chapter.title)
                .bindLong(ChapterTable.COL_MANGA_ID, mangaId)
                .execute()

            // Insert latest record.
            latestCacheExecutor.bindLong(LatestTable.COL_REMOTE_TASK_ID, cacheId)
                .bindLong(LatestTable.COL_MANGA_ID, mangaId)
                .bindString(LatestTable.COL_CHAPTER_ID, chapterId)
                .execute()
        }
    }

    private object Statements {

        fun selectLatest(cacheId: Long) = Select.from(LatestTable.NAME)
            .columns(
                LatestTable.COL_REMOTE_TASK_ID,
                LatestTable.COL_MANGA_ID,
                LatestTable.COL_CHAPTER_ID,
                MangaTable.COL_TITLE,
                MangaTable.COL_ALIAS,
                MangaTable.COL_IMAGE_URL,
                ChapterTable.COL_TITLE,
                ChapterTable.COL_NUMBER,
                ChapterTable.COL_RELEASE_DATE
            )
            .where(LatestTable.COL_REMOTE_TASK_ID.equalTo(cacheId))
            .innerJoin(ChapterTable.NAME, LatestTable.COL_CHAPTER_ID.equalTo(ChapterTable.COL_ID))
            .innerJoin(MangaTable.NAME, LatestTable.COL_MANGA_ID.equalTo(MangaTable.COL_ID))
            .orderDesc(ChapterTable.COL_RELEASE_DATE)
            .build()

        fun insertManga() = Insert.into(MangaTable.NAME)
            .conflictType(ConflictType.IGNORE)
            .columns(
                MangaTable.COL_ID,
                MangaTable.COL_ALIAS,
                MangaTable.COL_IMAGE_URL,
                MangaTable.COL_TITLE
            )
            .build()

        fun updateManga(mangaId: Long) = Update.table(MangaTable.NAME)
            .conflictType(ConflictType.IGNORE)
            .columns(
                MangaTable.COL_IMAGE_URL,
                MangaTable.COL_TITLE
            )
            .where(MangaTable.COL_ID.equalTo(mangaId))
            .build()

        fun insertChapter() = Insert.into(ChapterTable.NAME)
            .conflictType(ConflictType.REPLACE)
            .columns(
                ChapterTable.COL_ID,
                ChapterTable.COL_MANGA_ID,
                ChapterTable.COL_RELEASE_DATE,
                ChapterTable.COL_NUMBER,
                ChapterTable.COL_TITLE
            )
            .build()

        fun insertLatest() = Insert.into(LatestTable.NAME)
            .conflictType(ConflictType.IGNORE)
            .columns(
                LatestTable.COL_REMOTE_TASK_ID,
                LatestTable.COL_MANGA_ID,
                LatestTable.COL_CHAPTER_ID
            )
            .build()
    }
}