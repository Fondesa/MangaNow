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
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleObserver

/**
 * Base [AppCompatActivity] used in this application.
 * Defines the [ActivityViewDelegate] used to create the root view.
 *
 * @param V type of [ActivityViewDelegate] used to create the view.
 */
abstract class BaseActivity<out V : ActivityViewDelegate> : AppCompatActivity() {

    /**
     * [ActivityViewDelegate] that will be initialized with [createViewManager].
     * The [ActivityViewDelegate] will create the root view and will configure it.
     */
    protected val viewManager by lazy { createViewManager() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val rootView = viewManager.onCreateView(savedInstanceState)
        // Set the root view created with the ViewManager.
        setContentView(rootView)

        (viewManager as? LifecycleObserver)?.let {
            lifecycle.addObserver(it)
        }
        (viewManager as? OnViewCreatedDelegate)?.onViewCreated(rootView, savedInstanceState)

        onViewCreated(savedInstanceState)
    }

    override fun onBackPressed() {
        // Try to handle the back press with the view manager.
        val handled = (viewManager as? OnBackPressDelegate)?.onBackPress() ?: false
        if (!handled) {
            // If the back press wasn't handled, invoke the super handling.
            super.onBackPressed()
        }
    }

    /**
     * Creates the [ActivityViewDelegate] used to create and bind the root view.
     *
     * @return instance of [ActivityViewDelegate].
     */
    abstract fun createViewManager(): V

    /**
     * Called when the view is successfully created and configured by the [ActivityViewDelegate].
     *
     * @param savedInstanceState saved state [Bundle] passed in [onCreate].
     */
    abstract fun onViewCreated(savedInstanceState: Bundle?)
}