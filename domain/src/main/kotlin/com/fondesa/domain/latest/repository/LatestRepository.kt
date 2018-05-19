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

package com.fondesa.domain.latest.repository

import com.fondesa.domain.latest.LatestList
import com.fondesa.domain.latest.model.Latest

/**
 * Manages all the data operations about a [Latest] model.
 */
interface LatestRepository {

    /**
     * Gets the list of all the [Latest].
     *
     * @param page the page's number.
     * @param pageSize the size of the [page].
     * @return a list containing all the [Latest] in the current [page].
     */
    suspend fun getPaginated(page: Int, pageSize: Int): LatestList
}