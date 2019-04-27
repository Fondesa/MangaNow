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

package com.fondesa.manganow.latest.impl

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.fondesa.manganow.latest.impl.qualifiers.PageSize
import com.fondesa.manganow.log.api.Log
import com.fondesa.manganow.navigation.api.Router
import com.fondesa.manganow.thread.api.DispatchOnMainExceptionHandler
import com.fondesa.manganow.ui.api.qualifiers.ScreenScope
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@ScreenScope
class LatestPresenter @Inject constructor(
    private val view: LatestContract.View,
    @PageSize private val pageSize: Int,
    private val getLatestList: GetLatestList,
    private val router: Router
) : LatestContract.Presenter, CoroutineScope, LifecycleObserver {

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Default + DispatchOnMainExceptionHandler { t ->
            handleOperationsErrors(t)
        }

    private val job = SupervisorJob()

    private var currentPage = 0
    private val latestList = mutableListOf<Latest>()

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private fun detach() {
        job.cancel()
    }

    override fun attach() {
        // Bring the current page to 1 if other pages were loaded before.
        currentPage = 1
        // Clear the list other pages were loaded before.
        latestList.clear()

        // At the first page show the progress because there aren't elements.
        view.showProgressIndicator()
        view.hideListContainer()

        executeAsyncGetLatestList {
            view.showLatestManga(latestList)
            view.hideProgressIndicator()
            view.showListContainer()
        }
    }

    override fun pageEnded() {
        executeAsyncGetLatestList {
            view.showLatestManga(latestList)
        }
    }

    override fun latestClicked(latest: Latest) {
        Log.d("Clicked: ${latest.manga.title}")
    }

    private inline fun executeAsyncGetLatestList(crossinline onComplete: () -> Unit) {
        launch {
            val result = getLatestList.execute(currentPage, pageSize)
            latestList.addAll(result)

            // Increment the current page when the load completes.
            currentPage++

            withContext(Dispatchers.Main) {
                onComplete()
            }
        }
    }

    private fun handleOperationsErrors(t: Throwable) {
        view.showErrorForCause(t.toErrorCause())
        if (currentPage == 1) {
            view.hideProgressIndicator()
            view.showListContainer()
        }
    }
}