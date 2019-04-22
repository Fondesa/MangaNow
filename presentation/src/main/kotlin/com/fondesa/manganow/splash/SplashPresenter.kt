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

package com.fondesa.manganow.splash

import com.fondesa.data.converter.Converter
import com.fondesa.domain.category.CategoryList
import com.fondesa.domain.category.usecase.GetCategoryList
import com.fondesa.domain.sortorder.SortOrderList
import com.fondesa.domain.sortorder.usecase.GetSortOrderList
import com.fondesa.manganow.core.api.coroutines.trying
import com.fondesa.manganow.navigation.Navigator
import com.fondesa.manganow.navigation.Screen
import com.fondesa.manganow.ui.mvp.AbstractPresenter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

/**
 * Default implementation of [SplashContract.Presenter] for the splash section.
 */
class SplashPresenter @Inject constructor(
    private val getCategoryListUseCase: GetCategoryList,
    private val getSortOrderListUseCase: GetSortOrderList,
    private val scheduler: com.fondesa.manganow.time.api.Scheduler,
    private val throwableConverter: @JvmSuppressWildcards Converter<Throwable, String>,
    private val uiCoroutinesContext: CoroutineContext,
    private val navigator: Navigator
) : AbstractPresenter<SplashContract.View>(),
    SplashContract.Presenter,
    CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + job

    private val job = Job()

    private var openingTime = 0L

    private var canNavigate = true
    private var navigationWasDispatched = false

    override fun attachView(view: SplashContract.View) {
        super.attachView(view)
        // Save the millis before the request to calculate the timer gap.
        openingTime = System.currentTimeMillis()
        // Load the data.
        loadData()
    }

    override fun detachView() {
        scheduler.release()
        job.cancel()
        super.detachView()
    }

    override fun retryButtonClicked() {
        loadData()
    }

    override fun movedToForeground() {
        canNavigate = true
        if (navigationWasDispatched) {
            // Navigate to the main screen.
            executeNavigation()
            navigationWasDispatched = false
        }
    }

    override fun movedToBackground() {
        canNavigate = false
    }

    private fun loadData() {
        view.hideRetryButton()
        view.hideErrorMessage()
        view.showProgressIndicator()

        launch(uiCoroutinesContext) {
            trying {
                getCategoryListUseCase.execute()
            }.onSuccess(::onCategoriesLoadCompleted)
                .onError(::onLoadFailed)
        }
    }


    private fun onCategoriesLoadCompleted(categories: CategoryList) {
        if (!isViewAttached())
            return

        launch(uiCoroutinesContext) {
            trying {
                getSortOrderListUseCase.execute()
            }.onSuccess(::onSortOrdersLoadCompleted)
                .onError(::onLoadFailed)
        }
    }

    private fun onSortOrdersLoadCompleted(sortOrders: SortOrderList) {
        if (!isViewAttached())
            return

        // Calculate the time gap since the section was shown.
        var gapTime = System.currentTimeMillis() - openingTime
        // If the time is more than the minimum to navigate, the schedule will be immediate.
        if (gapTime > SPLASH_SCREEN_MS) {
            gapTime = SPLASH_SCREEN_MS
        }
        scheduler.schedule(SPLASH_SCREEN_MS - gapTime) {
            if (canNavigate) {
                // Navigate to the main screen.
                executeNavigation()
            } else {
                // Dispatch the navigation when the app comes again to foreground.
                navigationWasDispatched = true
            }
        }
    }

    private fun onLoadFailed(t: Throwable) {
        if (!isViewAttached())
            return

        view.hideProgressIndicator()

        val msg = throwableConverter.convert(t)
        view.showErrorMessage(msg)
        view.showRetryButton()
    }

    private fun executeNavigation() {
        view.hideProgressIndicator()
        navigator.goTo(Screen.Latest(), Navigator.Strategy.REPLACE_ALL)
    }

    companion object {

        /**
         * Minimum duration in milliseconds of this section.
         * Below this amount of time, the splash screen won't navigate to the next section and
         * it will move forward when this amount of time is reached.
         */
        private const val SPLASH_SCREEN_MS = 500L
    }
}