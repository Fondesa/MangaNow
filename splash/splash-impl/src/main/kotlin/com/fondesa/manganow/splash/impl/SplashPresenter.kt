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

package com.fondesa.manganow.splash.impl

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.fondesa.manganow.latest.api.LatestRoute
import com.fondesa.manganow.navigation.api.Router
import com.fondesa.manganow.splash.impl.category.GetCategoryList
import com.fondesa.manganow.splash.impl.sortorder.GetSortOrderList
import com.fondesa.manganow.thread.api.DispatchOnMainExceptionHandler
import com.fondesa.manganow.thread.api.launchWithDelay
import com.fondesa.manganow.ui.api.qualifiers.ScreenScope
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@ScreenScope
class SplashPresenter @Inject constructor(
    private val view: SplashContract.View,
    private val getCategoryList: GetCategoryList,
    private val getSortOrderList: GetSortOrderList,
    private val router: Router
) : SplashContract.Presenter, CoroutineScope, LifecycleObserver {

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Default + DispatchOnMainExceptionHandler { t ->
            handleOperationsErrors(t)
        }

    private val job = SupervisorJob()

    private var openingTime = 0L
    private var canNavigate = true
    private var navigationWasDispatched = false
    private var categoryOpCompleted = false
    private var sortOrderOpCompleted = false

    override fun attach() {
        // Save the millis before the request to calculate the timer gap.
        openingTime = System.currentTimeMillis()

        loadDataAndNavigate()
    }

    override fun retryButtonClicked() {
        loadDataAndNavigate()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private fun viewEntersForegroundState() {
        canNavigate = true
        if (canNavigate && navigationWasDispatched) {
            // Navigate to the main screen.
            executeNavigation()
            navigationWasDispatched = false
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    private fun viewEntersBackgroundState() {
        canNavigate = false
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private fun detach() {
        job.cancel()
    }

    private fun loadDataAndNavigate() {
        view.hideRetryButton()
        view.hideErrorMessage()
        view.showProgressIndicator()

        executeAsyncOperations {
            navigateToMainScreen()
        }
    }

    private inline fun executeAsyncOperations(crossinline onComplete: () -> Unit) {
        launch {
            val jobs = mutableListOf<Job>()
            if (!categoryOpCompleted) {
                jobs += launch {
                    getCategoryList.execute()
                    categoryOpCompleted = true
                }
            }

            if (!sortOrderOpCompleted) {
                jobs += launch {
                    getSortOrderList.execute()
                    sortOrderOpCompleted = true
                }
            }
            jobs.joinAll()

            withContext(Dispatchers.Main) {
                onComplete()
            }
        }
    }

    private fun navigateToMainScreen() {
        // Calculate the time taken by the request.
        var gapTime = System.currentTimeMillis() - openingTime
        // If the time is more than the minimum to navigate, the schedule will be immediate.
        if (gapTime > SPLASH_SCREEN_MS) {
            gapTime = SPLASH_SCREEN_MS
        }
        launchWithDelay(delay = SPLASH_SCREEN_MS - gapTime) {
            if (canNavigate) {
                // Navigate to the main screen.
                executeNavigation()
            } else {
                // Dispatch the navigation when the app comes again to foreground.
                navigationWasDispatched = true
            }
        }
    }

    private fun executeNavigation() {
        view.hideProgressIndicator()
        router.navigate(LatestRoute)
//        view.navigateToMainScreen()
    }

    private fun handleOperationsErrors(t: Throwable) {
        view.hideProgressIndicator()
        view.showErrorForCause(t.toErrorCause())
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