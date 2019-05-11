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
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.fondesa.manganow.ui.api.navigation.Navigator
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.navigation.NavigationView

abstract class BaseNavigationActivityViewDelegate(
    protected val activity: AppCompatActivity,
    private val navigator: Navigator
) : NavigationActivityViewDelegate,
    OnViewCreatedDelegate,
    OnBackPressDelegate,
    LifecycleObserver {

    override val coordinatorLayout: CoordinatorLayout
        get() = _coordinatorLayout ?: throw ViewNotInstantiatedException()

    private var _coordinatorLayout: CoordinatorLayout? = null

    override val appBarLayout: AppBarLayout
        get() = _appBarLayout ?: throw ViewNotInstantiatedException()

    private var _appBarLayout: AppBarLayout? = null

    override val toolbar: Toolbar
        get() = _toolbar ?: throw ViewNotInstantiatedException()

    private var _toolbar: Toolbar? = null

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView

    @IdRes
    private var screenNavItem: Int = 0

    private val drawerToggle: ActionBarDrawerToggle by lazy {
        ActionBarDrawerToggle(
            activity,
            drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
    }

    override fun onCreateView(savedInstanceState: Bundle?): View {
        // Inflate the base layout.
        val view = View.inflate(activity, R.layout.activity_root_navigation, null)
        // The root view will be full screen.
        view.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        drawerLayout = view.findViewById(R.id.drawerLayout)
        navigationView = view.findViewById(R.id.navigationView)
        _coordinatorLayout = view.findViewById(R.id.coordinator)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Attach the navigator adding the OnItemSelectedListener on the NavigationView.
        attachNavigator()

        // Get the AppBarLayout that will be added to the CoordinatorLayout.
        _appBarLayout = createAppBarLayout()
        // Get the content layout that will be added to the CoordinatorLayout.
        val contentLayout = createContentLayout()

        coordinatorLayout.addView(appBarLayout)
        coordinatorLayout.addView(contentLayout)

        _toolbar = view.findViewById(R.id.toolbar)
        // Set _toolbar as ActionBar.
        activity.setSupportActionBar(toolbar)

        drawerLayout.addDrawerListener(drawerToggle)

        // Necessary after the initialization to synchronize the state of the DrawerLayout with the button.
        drawerToggle.syncState()
    }

    override fun onBackPress(): Boolean {
        val drawerGravity = GravityCompat.START
        if (drawerLayout.isDrawerOpen(drawerGravity)) {
            // Close the drawer if the drawer is opened.
            drawerLayout.closeDrawer(drawerGravity)
            return true
        }
        return false
    }

    protected abstract fun createAppBarLayout(): AppBarLayout

    protected abstract fun createContentLayout(): View

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private fun onDestroy() {
        navigationView.setNavigationItemSelectedListener(null)
        drawerLayout.removeDrawerListener(drawerToggle)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private fun onStart() {
        // Check the screen related id if the user navigated to another screen before..
        navigationView.setCheckedItem(screenNavItem)
    }

    private fun attachNavigator() {
        screenNavItem = navigator.getCurrentItemId()
        // Restore the current selected id.
        navigationView.setCheckedItem(screenNavItem)

        // Attach the Navigator.
        navigationView.setNavigationItemSelectedListener { item ->
            navigator.onItemSelected(item.itemId)
            // Close drawer after click.
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }
}