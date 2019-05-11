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

package com.fondesa.manganow.ui.api.navigation

import android.app.Activity
import androidx.annotation.IdRes
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.fondesa.manganow.navigation.api.Route
import com.fondesa.manganow.navigation.api.Router
import com.fondesa.manganow.thread.api.launchWithDelay
import com.fondesa.manganow.ui.api.qualifiers.ScreenScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

// TODO: ActivityRetriever??
@ScreenScope
class RouteNavigator @Inject constructor(
    private val activity: Activity,
    private val router: Router,
    private val itemRouteMap: Map<NavigationSection, @JvmSuppressWildcards Route>,
    private val itemIdContainer: NavigatorItemIdContainer
) : Navigator, LifecycleObserver, CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Default

    private val job = Job()

    @IdRes
    private var screenNavItem: Int = 0

    override fun onItemSelected(@IdRes selectedId: Int) {
        screenNavItem = itemIdContainer.value
        if (screenNavItem == selectedId)
            return

        // Start the navigation 250ms after to not overlap the system animation.
        launchWithDelay(TRANSACTION_DELAY_MS) {
            val section = NavigationSection.values().find {
                it.itemId == selectedId
            } ?: throw IllegalArgumentException(
                "Can't find a ${NavigationSection::class.java.name} for the selected id."
            )
            val route = itemRouteMap[section]
                ?: throw IllegalStateException("You should specify a route for the given id.")

            if (itemIdContainer.value != NavigatorItemIdContainer.START_NAVIGATION_ID) {
                // Finish the current Activity if it isn't the main section.
                activity.finish()
            }

            itemIdContainer.value = selectedId

            // Navigate to the given route.
            router.navigate(route)
            // Remove animations
            activity.overridePendingTransition(0, 0)
        }
    }

    @IdRes
    override fun getCurrentItemId() = itemIdContainer.value

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private fun onStart() {
        if (screenNavItem != 0) {
            // Save the screen related id if the user navigated to another screen before.
            itemIdContainer.value = screenNavItem
            screenNavItem = 0
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private fun detach() {
        // Release the Handler callback.
        job.cancel()
    }

    companion object {
        private const val TRANSACTION_DELAY_MS = 250L
    }
}