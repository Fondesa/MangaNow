package com.fondesa.manganow.ui.screen.navigation

import android.app.Activity
import androidx.annotation.IdRes
import com.fondesa.manganow.log.api.Log
import com.fondesa.manganow.navigation.api.Route
import com.fondesa.manganow.navigation.api.Router
import com.fondesa.manganow.time.api.Scheduler

/**
 * Implementation of [Navigator] used to navigate to new section through [Route]s.
 * This [Navigator] gives a major priority to the start id (used to identify the main section).
 * The other sections have equal priority.
 * Given the section A as the main section and the sections B and C as two others sections,
 * when the user navigates from A to B and from B to C, the stack contains only A and C.
 */
class RouteNavigator(
    @IdRes private val startNavigationId: Int,
    private val router: Router,
    private val itemRouteMap: Map<Int, Route>,
    private val scheduler: Scheduler
) : Navigator {

    @IdRes
    private var currentId = DEFAULT_START_ID
    private var activity: Activity? = null

    override fun attach(activity: Activity) {
        this.activity = activity
        // Get the current selected id from Intent.
        //TODO
//        currentId = activity.intent.getIntExtra(ARG_CURRENT_ITEM, startNavigationId())
    }

    override fun detach() {
        // Release the Handler callback.
        scheduler.release()
        activity = null
    }

    override fun onItemSelected(@IdRes selectedId: Int) {
        if (currentId == selectedId)
            return

        val runnableBlock = {
            val activity = this.activity
            if (activity != null) {
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
            } else {
                Log.d("The activity is null.")
            }
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