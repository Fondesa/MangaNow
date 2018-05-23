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

package com.fondesa.manganow.latest

import com.fondesa.common.coroutines.CoroutinesJobsPool
import com.fondesa.common.coroutines.inPool
import com.fondesa.common.coroutines.trying
import com.fondesa.common.log.Logger
import com.fondesa.data.converter.Converter
import com.fondesa.domain.latest.model.Latest
import com.fondesa.domain.latest.usecase.GetLatestList
import com.fondesa.manganow.navigation.Navigator
import com.fondesa.manganow.presenter.AbstractPresenter
import kotlinx.coroutines.experimental.launch
import javax.inject.Inject
import kotlin.coroutines.experimental.CoroutineContext

/**
 * Default implementation of [LatestContract.Presenter] for the latest section.
 */
class LatestPresenter @Inject constructor(
    private val logger: Logger,
    private val getLatestListUseCase: GetLatestList,
    private val throwableConverter: @JvmSuppressWildcards Converter<Throwable, String>,
    private val uiCoroutinesContext: CoroutineContext,
    private val navigator: Navigator
) : AbstractPresenter<LatestContract.View>(),
    LatestContract.Presenter {

    private val latestList = mutableListOf<Latest>()
    private var currentPage = 0
    private var currentExecutingPage: Int? = null
    private val jobsPool by lazy { CoroutinesJobsPool() }

    override fun attachView(view: LatestContract.View) {
        super.attachView(view)
        // Bring the current page to 1 if other pages were loaded before.
        currentPage = 1
        // Clear the list other pages were loaded before.
        latestList.clear()

        // At the first page show the progress because there aren't elements.
        view.showProgressIndicator()
        view.hideListContainer()

        loadNextPage()
    }

    override fun detachView() {
        jobsPool.cancelAll()
        super.detachView()
    }

    override fun pageEnded() {
        if (currentExecutingPage != currentPage) {
            loadNextPage()
        }
    }

    override fun latestSelected(latest: Latest) {
        logger.d("Selected: ${latest.manga.title}")
    }

    private fun loadNextPage() {
        currentExecutingPage = currentPage

        launch(uiCoroutinesContext) {
            trying {
                getLatestListUseCase.execute(currentPage, LatestContract.PAGE_SIZE)
            }.onSuccess(::onLatestLoadCompleted)
                .onError(::onLatestLoadFailed)
        }.inPool(jobsPool)
    }

    private fun onLatestLoadCompleted(result: List<Latest>) {
        if (!isViewAttached())
            return

        currentExecutingPage = null

        latestList.addAll(result)

        view.updateLatestList(latestList)

        if (isFirstLoad()) {
            view.hideProgressIndicator()
            view.showListContainer()
        }

        // Increment the current page when the load completes.
        currentPage++
    }

    private fun onLatestLoadFailed(t: Throwable) {
        if (!isViewAttached())
            return

        currentExecutingPage = null

        val msg = throwableConverter.convert(t)
        view.showErrorMessage(msg)

        if (isFirstLoad()) {
            view.hideProgressIndicator()
            view.showListContainer()
        }
    }

    private fun isFirstLoad() = currentPage == 1
}