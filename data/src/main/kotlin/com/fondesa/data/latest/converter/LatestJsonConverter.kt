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

package com.fondesa.data.latest.converter

import com.fondesa.domain.latest.LatestList
import com.fondesa.domain.latest.model.Latest
import com.fondesa.manganow.domain.chapter.Chapter
import com.fondesa.manganow.domain.manga.Manga
import com.fondesa.manganow.serialization.api.json.asNullableString
import com.fondesa.manganow.serialization.api.json.mapJsonObject
import com.fondesa.manganow.storage.api.remote.RemoteStorageConverter
import com.google.gson.JsonElement
import java.util.*
import javax.inject.Inject

class LatestJsonConverter @Inject constructor() : RemoteStorageConverter<LatestList> {

    override fun convert(value: JsonElement): LatestList = value.asJsonArray.mapJsonObject {
        val manga = Manga(
            id = it["id"].asLong,
            alias = it["alias"].asString,
            imageUrl = it["image"].asNullableString,
            title = it["title"].asString
        )

        val chapterJson = it["lastChapter"].asJsonObject
        val chapter = Chapter(
            id = chapterJson["id"].asString,
            releaseDate = Date(chapterJson["releaseDate"].asLong),
            number = chapterJson["number"].asDouble,
            title = chapterJson["title"].asNullableString
        )

        Latest(manga, chapter)
    }
}