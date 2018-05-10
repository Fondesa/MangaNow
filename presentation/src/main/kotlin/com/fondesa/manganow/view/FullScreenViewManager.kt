package com.fondesa.manganow.view

import android.support.annotation.LayoutRes
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CoordinatorLayout
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import com.fondesa.manganow.R

/**
 * Implementation of [ViewManager] used to show a fullscreen section.
 * The [contentLayout] will be inflated ignoring the root layout params defined in XML.
 *
 * @param contentLayout layout resource of the section.
 */
class FullScreenViewManager(@LayoutRes private val contentLayout: Int, val fitsSystemWindows: Boolean) : ViewManager {

    /**
     * Instance of [CoordinatorLayout] that contains the [AppBarLayout] and the content layout.
     * The [CoordinatorLayout] will be available only after [bind].
     */
    lateinit var coordinatorLayout: CoordinatorLayout
        private set

    override fun createRootView(activity: AppCompatActivity): ViewGroup {
        // Inflate the content layout.
        val view = View.inflate(activity, R.layout.screen_base_drawer, null)
        // The root view will be full screen.
        view.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        view.fitsSystemWindows = fitsSystemWindows
        return view as ViewGroup
    }

    override fun bind(activity: AppCompatActivity, rootView: ViewGroup) {
        coordinatorLayout = rootView.findViewById(R.id.coordinator)
        // Inflate the content layout into the CoordinatorLayout.
        View.inflate(activity, contentLayout, coordinatorLayout)
    }

    override fun detach(activity: AppCompatActivity, rootView: ViewGroup) {
        // Empty implementation.
    }
}