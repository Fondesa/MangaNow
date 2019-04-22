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

package com.fondesa.data.category.converter

import com.fondesa.data.serialization.FromJsonConverter
import com.fondesa.data.serialization.mapJsonObject
import com.fondesa.domain.category.CategoryList
import com.fondesa.manganow.domain.category.Category
import com.google.gson.JsonElement
import javax.inject.Inject

class CategoryJsonConverter @Inject constructor() : FromJsonConverter<CategoryList> {

    override fun convert(value: JsonElement): CategoryList = value.asJsonArray.mapJsonObject {
        Category(
            id = it["id"].asLong,
            name = it["name"].asString
        )
    }
}