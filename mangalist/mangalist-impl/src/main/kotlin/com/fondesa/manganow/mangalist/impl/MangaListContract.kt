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

import com.fondesa.manganow.domain.manga.Manga
import com.fondesa.manganow.mangalist.api.sortorder.SortOrder
import com.fondesa.manganow.mangalist.api.sortorder.SortOrderList
import com.fondesa.manganow.ui.api.mvp.BasePresenter

object MangaListContract {

    interface View {

        fun showProgressIndicator()

        fun hideProgressIndicator()

        fun showListContainer()

        fun hideListContainer()

        fun showErrorForCause(cause: ErrorCause)

        fun showZeroElementsView()

        fun hideZeroElementsView()

        fun showMangaList(mangaList: MangaList)

        fun showSortOrdersView()

        fun hideSortOrdersView()

        fun showSortOrders(sortOrders: SortOrderList, defaultSortOrder: SortOrder)
    }

    interface Presenter : BasePresenter {

        fun pageEnded()

        fun sortOrderSelected(sortOrder: SortOrder)

        fun textSearched(text: String)

        fun mangaClicked(manga: Manga)
    }
}