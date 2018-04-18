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

import com.fondesa.domain.category.model.Category
import com.fondesa.domain.sortorder.model.SortOrder
import com.fondesa.domain.usecase.UseCase
import com.fondesa.manganow.converter.Converter
import com.fondesa.manganow.presenter.AbstractPresenter
import com.fondesa.manganow.time.Scheduler
import com.fondesa.thread.execution.ExecutorFactory
import com.fondesa.thread.execution.ExecutorPool
import com.fondesa.thread.execution.execute
import javax.inject.Inject

/**
 * Default implementation of [SplashContract.Presenter] for the splash section.
 */
class SplashPresenter @Inject constructor(
    private val getSortOrderListUseCase: @JvmSuppressWildcards UseCase<List<SortOrder>, Unit>,
    private val scheduler: Scheduler,
    private val throwableConverter: @JvmSuppressWildcards Converter<Throwable, String>,
    private val executorFactory: ExecutorFactory,
    private val executorPool: ExecutorPool
) : AbstractPresenter<SplashContract.View>(),
    SplashContract.Presenter {

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
        // Release the scheduler.
        scheduler.release()
        // Release the executors.
        executorPool.release()
        super.detachView()
    }

    override fun retryButtonClicked() {
        loadData()
    }

    override fun allowNavigation(allow: Boolean) {
        canNavigate = allow
        if (canNavigate && navigationWasDispatched) {
            // Navigate to the main screen.
            executeNavigation()
            navigationWasDispatched = false
        }
    }

    private fun loadData() {
        view.hideRetryButton()
        view.hideErrorMessage()
        view.showProgressIndicator()

        // Create and load the executor used to load the categories.
        executorFactory
            .create {
                getSortOrderListUseCase.execute(Unit)
            }
            .completed(::onSortOrdersLoadCompleted)
            .error(::onSortOrdersLoadFailed)
            .execute(executorPool)
    }

    private fun onCategoriesLoadCompleted(categories: Array<Category>) {
        if (!isViewAttached())
            return

        // Create and load the task used to download the sort orders.
        //TODO:
    }

    private fun onCategoriesLoadFailed(e: Exception) {
        manageOnLoadFailed(e)
    }

    private fun onSortOrdersLoadCompleted(sortOrders: List<SortOrder>) {
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

    private fun onSortOrdersLoadFailed(t: Throwable) {
        manageOnLoadFailed(t)
    }

    private fun executeNavigation() {
        view.hideProgressIndicator()
        view.navigateToMainScreen()
    }

    private fun manageOnLoadFailed(t: Throwable) {
        if (!isViewAttached())
            return

        view.hideProgressIndicator()

        val msg = throwableConverter.convert(t)
        view.showErrorMessage(msg)
        view.showRetryButton()
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