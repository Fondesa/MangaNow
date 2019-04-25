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

package com.fondesa.manganow.serialization.api.json

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive

/**
 * Helper val used to parse a [JsonPrimitive] from a [JsonElement].
 * The value is null if the [JsonElement] is null.
 */
val JsonElement?.asNullableJsonPrimitive: JsonPrimitive?
    get() = if (this == null || isJsonNull) null else asJsonPrimitive

/**
 * Helper val used to parse a [JsonObject] from a [JsonElement].
 * The value is null if the [JsonElement] is null.
 */
val JsonElement?.asNullableJsonObject: JsonObject?
    get() = if (this == null || isJsonNull) null else asJsonObject

/**
 * Helper val used to parse a [JsonArray] from a [JsonElement].
 * The value is null if the [JsonElement] is null.
 */
val JsonElement?.asNullableJsonArray: JsonArray?
    get() = if (this == null || isJsonNull) null else asJsonArray

/**
 * Helper val used to parse a [Boolean] from a [JsonElement].
 * The value is null if the [JsonElement] is null.
 */
val JsonElement?.asNullableBoolean: Boolean?
    get() = asNullableJsonPrimitive?.asBoolean

/**
 * Helper val used to parse a [Int] from a [JsonElement].
 * The value is null if the [JsonElement] is null.
 */
val JsonElement?.asNullableInt: Int?
    get() = asNullableJsonPrimitive?.asInt

/**
 * Helper val used to parse a [Long] from a [JsonElement].
 * The value is null if the [JsonElement] is null.
 */
val JsonElement?.asNullableLong: Long?
    get() = asNullableJsonPrimitive?.asLong

/**
 * Helper val used to parse a [Double] from a [JsonElement].
 * The value is null if the [JsonElement] is null.
 */
val JsonElement?.asNullableDouble: Double?
    get() = asNullableJsonPrimitive?.asDouble

/**
 * Helper val used to parse a [String] from a [JsonElement].
 * The value is null if the [JsonElement] is null.
 */
val JsonElement?.asNullableString: String?
    get() = asNullableJsonPrimitive?.asString

inline fun <T> JsonArray.mapJsonObject(transform: (JsonObject) -> T): List<T> = map {
    transform(it.asJsonObject)
}