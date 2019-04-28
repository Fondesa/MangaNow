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

package com.fondesa.manganow.mangalist.api.sortorder

/**
 * Defines the operation which must be executed to obtain the list of the sort orders.
 */
interface GetSortOrderList {

    /**
     * Gets the list of all the sort order.
     *
     * @return a list containing all the possible sort orders used to sort a list of manga.
     */
    suspend fun execute(): SortOrderList
}