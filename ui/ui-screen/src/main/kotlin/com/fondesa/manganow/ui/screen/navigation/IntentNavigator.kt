package com.fondesa.manganow.ui.screen.navigation

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Handler
import androidx.annotation.IdRes
import com.fondesa.manganow.log.api.Log

/**
 * Implementation of [Navigator] used to navigate to new section through [Intent]s.
 * This [Navigator] gives a major priority to the start id (used to identify the main section).
 * The other sections have equal priority.
 * Given the section A as the main section and the sections B and C as two others sections,
 * when the user navigates from A to B and from B to C, the stack contains only A and C.
 */
abstract class IntentNavigator : Navigator {

    companion object {
        const val DEFAULT_START_ID = -1
        const val ARG_CURRENT_ITEM = "argCurrentItem"
        private const val TRANSACTION_DELAY_MS = 250L
    }

    @IdRes
    private var currentId = DEFAULT_START_ID
    private val navigationHandler by lazy { Handler() }
    private var activity: Activity? = null

    override fun attach(activity: Activity) {
        this.activity = activity
        // Get the current selected id from Intent.
        currentId = activity.intent.getIntExtra(ARG_CURRENT_ITEM, startNavigationId())
    }

    override fun detach() {
        // Release the Handler callback.
        navigationHandler.removeCallbacksAndMessages(null)
        activity = null
    }

    override fun onItemSelected(@IdRes selectedId: Int) {
        if (currentId == selectedId)
            return

        val runnableBlock = {
            val activity = this.activity
            if (activity != null) {
                val intent = intentOf(activity, selectedId)
                // Put the selected id in the Intent to restore it after.
                intent.putExtra(ARG_CURRENT_ITEM, selectedId)

                @IdRes val startId = startNavigationId()
                if (selectedId == startId) {
                    // Clear the stack when the user navigates to the main section.
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                } else if (currentId != startId) {
                    // Finish the current Activity if it isn't the main section.
                    activity.finish()
                }
                activity.startActivity(intent)
                // Remove animations
                activity.overridePendingTransition(0, 0)
            } else {
                Log.d("The activity is null.")
            }
        }
        // Start the navigation 250ms after to not overlap the system animation.
        navigationHandler.postDelayed(runnableBlock, TRANSACTION_DELAY_MS)
    }

    @IdRes
    override fun getCurrentItemId() = currentId

    /**
     * Get the id that identifies the main section of the application.
     * This section has more priority than the others because it will not be removed from the stack.
     *
     * @return id of the item related to the main section.
     */
    @IdRes
    abstract fun startNavigationId(): Int

    /**
     * Specify the [Intent] that must be launched when an item with the id [itemId] is clicked.
     *
     * @param context [Context] used to generate the [Intent].
     * @param itemId id of the selected item.
     */
    abstract fun intentOf(context: Context, @IdRes itemId: Int): Intent
}