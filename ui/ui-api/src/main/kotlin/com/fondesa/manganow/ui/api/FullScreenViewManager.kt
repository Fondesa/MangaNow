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

package com.fondesa.manganow.ui.api

import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout

/**
 * Implementation of [ViewManager] used to show a fullscreen section.
 * The [contentLayout] will be inflated ignoring the root layout params defined in XML.
 *
 * @param contentLayout layout resource of the section.
 */
class FullScreenViewManager(@LayoutRes private val contentLayout: Int, val fitsSystemWindows: Boolean) :
    ViewManager {

    /**
     * Instance of [CoordinatorLayout] which contains the content layout.
     * The [CoordinatorLayout] will be available only after [bind].
     */
    lateinit var coordinatorLayout: CoordinatorLayout
        private set

    override fun createRootView(activity: AppCompatActivity): ViewGroup {
        // Inflate the content layout.
        val view = View.inflate(activity, R.layout.activity_root_full_screen, null)
        // The root view will be full screen.
        view.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        view.fitsSystemWindows = fitsSystemWindows
        return view as ViewGroup
    }

    override fun bind(activity: AppCompatActivity, rootView: ViewGroup) {
        coordinatorLayout = rootView.findViewById(R.id.coordinator)

        // Get the content layout that will be added to the CoordinatorLayout.
        val contentLayout = View.inflate(activity, contentLayout, null)
        val layoutParams = CoordinatorLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        coordinatorLayout.addView(contentLayout, layoutParams)
    }

    override fun detach(activity: AppCompatActivity, rootView: ViewGroup) {
        // Empty implementation.
    }
}