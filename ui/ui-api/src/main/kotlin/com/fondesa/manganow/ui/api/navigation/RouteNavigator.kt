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
import com.fondesa.manganow.time.api.Scheduler

class RouteNavigator(
    private val activity: Activity,
    @IdRes private val startNavigationId: Int,
    private val router: Router,
    private val itemRouteMap: Map<Int, Route>,
    private val scheduler: Scheduler
) : Navigator, LifecycleObserver {

    @IdRes
    private var currentId = DEFAULT_START_ID


    //TODO initial id from singleton in init

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private fun detach() {
        // Release the Handler callback.
        scheduler.release()
    }

    override fun onItemSelected(@IdRes selectedId: Int) {
        if (currentId == selectedId)
            return

        val runnableBlock = {
            val route = itemRouteMap[selectedId] ?: throw
            IllegalStateException("You should specify a route for the given id.")
            // Put the selected id in the Intent to restore it after.
            // TODO use singleton
//                intent.putExtra(ARG_CURRENT_ITEM, selectedId)

            // TODO handle start id
//                if (selectedId == startId) {
            // Clear the stack when the user navigates to the main section.
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
//                }

            if (currentId != startNavigationId) {
                // Finish the current Activity if it isn't the main section.
                activity.finish()
            }

            // Navigate to the given route.
            router.navigate(route)
            // Remove animations
            activity.overridePendingTransition(0, 0)
        }
        // Start the navigation 250ms after to not overlap the system animation.
        scheduler.schedule(TRANSACTION_DELAY_MS, runnableBlock)
    }

    @IdRes
    override fun getCurrentItemId() = currentId

    companion object {
        const val DEFAULT_START_ID = -1
        //        const val ARG_CURRENT_ITEM = "argCurrentItem"
        private const val TRANSACTION_DELAY_MS = 250L
    }
}