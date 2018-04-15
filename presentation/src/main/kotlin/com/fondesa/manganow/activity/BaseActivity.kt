/*
 * Copyright (c) 2018 Fondesa
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

package com.fondesa.manganow.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.ViewGroup
import com.fondesa.manganow.view.BackPressHandler
import com.fondesa.manganow.view.ViewManager
import dagger.android.support.DaggerAppCompatActivity

/**
 * Base [AppCompatActivity] used in this application.
 * Defines the [ViewManager] used to create the root view.
 *
 * @param V type of [ViewManager] used to create the view.
 */
abstract class BaseActivity<out V : ViewManager> : DaggerAppCompatActivity() {

    /**
     * [ViewManager] that will be initialized with [createViewManager].
     * The [ViewManager] will create the root view and will configure it.
     */
    protected val viewManager by lazy { createViewManager() }

    /**
     * Root view of this [AppCompatActivity] created with the [viewManager].
     */
    protected val rootView by lazy { viewManager.createRootView(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set the root view created with the ViewManager.
        setContentView(rootView)

        // Configure the view.
        viewManager.bind(this, rootView)

        onViewCreated(rootView, savedInstanceState)
    }

    override fun onDestroy() {
        // Release the view resources.
        viewManager.detach(this, rootView)
        super.onDestroy()
    }

    override fun onBackPressed() {
        // Try to handle the back press with the view manager.
        val handled = (viewManager as? BackPressHandler)?.handleBackPress() ?: false
        if (!handled) {
            // If the back press wasn't handled, invoke the super handling.
            super.onBackPressed()
        }
    }

    /**
     * Creates the [ViewManager] used to create and bind the root view.
     *
     * @return instance of [ViewManager].
     */
    abstract fun createViewManager(): V

    /**
     * Called when the view is successfully created and configured by the [ViewManager].
     *
     * @param view root view created by the [ViewManager].
     * @param savedInstanceState saved state [Bundle] passed in [onCreate].
     */
    abstract fun onViewCreated(view: ViewGroup, savedInstanceState: Bundle?)
}