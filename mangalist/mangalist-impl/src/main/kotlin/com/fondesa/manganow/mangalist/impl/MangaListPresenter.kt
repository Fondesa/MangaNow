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

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.fondesa.manganow.domain.manga.Manga
import com.fondesa.manganow.log.api.Log
import com.fondesa.manganow.mangalist.api.sortorder.GetSortOrderList
import com.fondesa.manganow.mangalist.api.sortorder.SortOrder
import com.fondesa.manganow.mangalist.api.sortorder.SortOrderList
import com.fondesa.manganow.mangalist.impl.qualifiers.PageSize
import com.fondesa.manganow.navigation.api.Router
import com.fondesa.manganow.thread.api.dispatchOnMainExceptionHandler
import com.fondesa.manganow.thread.api.printStacktraceExceptionHandler
import com.fondesa.manganow.ui.api.qualifiers.ScreenScope
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@ScreenScope
class MangaListPresenter @Inject constructor(
    private val view: MangaListContract.View,
    @PageSize private val pageSize: Int,
    private val getMangaList: GetMangaList,
    private val getSortOrderList: GetSortOrderList,
    private val router: Router
) : MangaListContract.Presenter, CoroutineScope, LifecycleObserver {

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Default + printStacktraceExceptionHandler()

    private val job = SupervisorJob()
    private val mangaListExcHandler = dispatchOnMainExceptionHandler { t ->
        handleMangaListOperationsErrors(t)
    }

    private var currentPage = 0
    private val mangaList = mutableListOf<Manga>()
    private var sortOrder: SortOrder? = null
    private var textFilter: String? = null
    private var listViewState: ListViewState = ListViewState.INITIAL

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private fun detach() {
        job.cancel()
    }

    override fun attach() {
        loadFullMangaList()
        view.hideSortOrdersView()
        executeAsyncGetSortOrderList {
            if (it.isEmpty()) {
                return@executeAsyncGetSortOrderList
            }

            view.showSortOrdersView()
            view.showSortOrders(it, it.first())
        }
    }

    override fun pageEnded() {
        executeAsyncGetMangaList {
            view.showMangaList(mangaList)
        }
    }

    override fun sortOrderSelected(sortOrder: SortOrder) {
        this.sortOrder = sortOrder
        loadFullMangaList()
    }

    override fun textSearched(text: String) {
        textFilter = text
        loadFullMangaList()
    }

    override fun mangaClicked(manga: Manga) {
        Log.d("Clicked: ${manga.title}")
    }

    private fun loadFullMangaList() {
        // Bring the current page to 1 if other pages were loaded before.
        currentPage = 1
        // Clear the list other pages were loaded before.
        mangaList.clear()

        // At the first page show the progress because there aren't elements.
        view.showProgressIndicator()
        view.hideListContainer()
        view.hideZeroElementsView()
        executeAsyncGetMangaList {
            view.showMangaList(mangaList)
            view.hideProgressIndicator()
            if (mangaList.isEmpty()) {
                view.showZeroElementsViewChangingState()
            } else {
                view.showListContainerChangingState()
            }
        }
    }

    private inline fun executeAsyncGetMangaList(crossinline onComplete: () -> Unit) {
        launch(mangaListExcHandler) {
            val result = getMangaList.execute(currentPage, pageSize, sortOrder, textFilter)
            mangaList.addAll(result)

            // Increment the current page when the load completes.
            currentPage++

            withContext(Dispatchers.Main) {
                onComplete()
            }
        }
    }

    private inline fun executeAsyncGetSortOrderList(crossinline onComplete: (SortOrderList) -> Unit) {
        launch {
            val result = getSortOrderList.execute()

            withContext(Dispatchers.Main) {
                onComplete(result)
            }
        }
    }

    private fun handleMangaListOperationsErrors(t: Throwable) {
        view.showErrorForCause(t.toErrorCause())
        if (currentPage == 1) {
            view.hideProgressIndicator()
            if (listViewState == ListViewState.ZERO_ELEM) {
                view.showZeroElementsView()
            } else if (listViewState == ListViewState.SOME_ELEM) {
                view.showListContainer()
            }
        }
    }

    private fun MangaListContract.View.showZeroElementsViewChangingState() {
        showZeroElementsView()
        listViewState = ListViewState.ZERO_ELEM
    }

    private fun MangaListContract.View.showListContainerChangingState() {
        showListContainer()
        listViewState = ListViewState.SOME_ELEM
    }

    private enum class ListViewState {
        INITIAL, ZERO_ELEM, SOME_ELEM
    }
}