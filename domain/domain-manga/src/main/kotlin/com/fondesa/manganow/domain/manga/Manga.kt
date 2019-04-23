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

/**
 *
 */
data class Manga(
    val id: Long,
    val alias: String,
    val author: String? = null,
    val description: String? = null,
    val hits: Int = 0,
    val imageUrl: String?,
    val yearOfRelease: Int? = null,
    val status: Status = Status.UNKNOWN,
    val title: String
) {

    enum class Status(val value: Long) {
        UNKNOWN(-1),
        DROPPED(0),
        ON_GOING(1),
        FINISHED(2)
    }
}