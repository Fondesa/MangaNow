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
import com.fondesa.manganow.splash.impl.category.GetCategoryList
import com.fondesa.manganow.time.api.Scheduler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class SplashPresenter @Inject constructor(
    private val view: SplashContract.View,
    private val getCategoryList: GetCategoryList,
    private val scheduler: Scheduler
) : SplashContract.Presenter, CoroutineScope, LifecycleObserver {

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    private val job = Job()

    private var openingTime = 0L

    private var canNavigate = true
    private var navigationWasDispatched = false

    override fun attach() {
        // Save the millis before the request to calculate the timer gap.
        openingTime = System.currentTimeMillis()

        view.hideRetryButton()
        view.hideErrorMessage()
        view.showProgressIndicator()

        // Create and load the task used to download the categories and sort orders.
        // TODO
        async {
            getCategoryList.execute()
        }
    }

    override fun retryButtonClicked() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private fun detach() {
        // Release the scheduler.
        scheduler.release()
        job.cancel()
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

    private fun onCategoriesLoadFailed(e: Exception) {
        manageOnLoadFailed(e)
    }

//    private fun onSortOrdersLoadCompleted(sortOrders: Array<SortOrder>) {
//        // Calculate the time taken by the request.
//        var gapTime = System.currentTimeMillis() - openingTime
//        // If the time is more than the minimum to navigate, the schedule will be immediate.
//        if (gapTime > SPLASH_SCREEN_MS) {
//            gapTime = SPLASH_SCREEN_MS
//        }
//        scheduler.schedule(SPLASH_SCREEN_MS - gapTime) {
//            if (canNavigate) {
//                // Navigate to the main screen.
//                executeNavigation()
//            } else {
//                // Dispatch the navigation when the app comes again to foreground.
//                navigationWasDispatched = true
//            }
//        }
//    }

    private fun onSortOrdersLoadFailed(e: Exception) {
        manageOnLoadFailed(e)
    }

    private fun executeNavigation() {
        view.hideProgressIndicator()
        view.navigateToMainScreen()
    }

    private fun manageOnLoadFailed(e: Exception) {
        view.hideProgressIndicator()

//        val msg = errorConverter.convert(e)
//        view.showErrorMessage(msg)
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