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
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleObserver
import dagger.android.AndroidInjection
import javax.inject.Inject

/**
 * Base [AppCompatActivity] used in this application.
 * Defines the [ActivityViewDelegate] used to create the root view.
 *
 * @param V type of [ActivityViewDelegate] used to create the view.
 */
abstract class BaseActivity<V : ActivityViewDelegate> : AppCompatActivity() {

    @Inject
    lateinit var viewManager: V
        internal set

    private val dispatchedMenuActions = mutableListOf<Menu.() -> Unit>()
    private var menu: Menu? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
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

    final override fun onCreateOptionsMenu(menu: Menu): Boolean {
        this.menu = menu
        dispatchedMenuActions.forEach { it(menu) }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onBackPressed() {
        // Try to handle the back press with the view manager.
        val handled = (viewManager as? OnBackPressDelegate)?.onBackPress() ?: false
        if (!handled) {
            // If the back press wasn't handled, invoke the super handling.
            super.onBackPressed()
        }
    }

    protected fun dispatchAfterMenuCreation(action: Menu.() -> Unit) {
        val menu = menu
        if (menu == null) {
            dispatchedMenuActions.add(action)
        } else {
            action(menu)
        }
    }

    /**
     * Called when the view is successfully created and configured by the [ActivityViewDelegate].
     *
     * @param savedInstanceState saved state [Bundle] passed in [onCreate].
     */
    abstract fun onViewCreated(savedInstanceState: Bundle?)
}