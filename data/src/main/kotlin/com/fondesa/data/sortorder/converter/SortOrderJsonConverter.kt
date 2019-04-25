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

package com.fondesa.data.sortorder.converter

import com.fondesa.domain.sortorder.SortOrderList
import com.fondesa.domain.sortorder.model.SortOrder
import com.fondesa.manganow.serialization.api.json.mapJsonObject
import com.fondesa.manganow.storage.api.remote.RemoteStorageConverter
import com.google.gson.JsonElement
import javax.inject.Inject

class SortOrderJsonConverter @Inject constructor() : RemoteStorageConverter<SortOrderList> {

    override fun convert(value: JsonElement): SortOrderList = value.asJsonArray.mapJsonObject {
        SortOrder(
            id = it["id"].asLong,
            name = it["name"].asString,
            priority = it["priority"].asInt
        )
    }
}