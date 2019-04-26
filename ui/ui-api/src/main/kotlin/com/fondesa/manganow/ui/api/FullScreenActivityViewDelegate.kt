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

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout

/**
 * Implementation of [ActivityViewDelegate] used to show a fullscreen section.
 * The [contentLayout] will be inflated ignoring the root layout params defined in XML.
 *
 * @param contentLayout layout resource of the section.
 */
class FullScreenActivityViewDelegate(
    private val activity: AppCompatActivity,
    @LayoutRes private val contentLayout: Int,
    private val fitsSystemWindows: Boolean
) : ActivityViewDelegate {

    /**
     * Instance of [CoordinatorLayout] which contains the content layout.
     */
    lateinit var coordinatorLayout: CoordinatorLayout
        private set

    override fun onCreateView(savedInstanceState: Bundle?): View =
        CoordinatorLayout(activity).apply {
            // The root view will be full screen.
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            fitsSystemWindows = this@FullScreenActivityViewDelegate.fitsSystemWindows
            // Get the content layout that will be added to the CoordinatorLayout.
            LayoutInflater.from(activity).inflate(contentLayout, this, true)
            coordinatorLayout = this
        }
}