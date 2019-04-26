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

abstract class NavigationActivityViewDelegate(protected val activity: AppCompatActivity) :
    ActivityViewDelegate,
    OnViewCreatedDelegate,
    OnBackPressDelegate,
    LifecycleObserver {

    lateinit var coordinatorLayout: CoordinatorLayout
        private set

    lateinit var appBarLayout: AppBarLayout
        private set

    lateinit var toolbar: Toolbar
        private set

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView

    private val navigator: Navigator by lazy {
        createNavigator()
    }
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
        coordinatorLayout = view.findViewById(R.id.coordinator)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Attach the navigator adding the OnItemSelectedListener on the NavigationView.
        navigator.attach(navigationView)

        // Get the AppBarLayout that will be added to the CoordinatorLayout.
        appBarLayout = createAppBarLayout()
        // Get the content layout that will be added to the CoordinatorLayout.
        val contentLayout = createContentLayout()

        coordinatorLayout.addView(appBarLayout)
        coordinatorLayout.addView(contentLayout)

        toolbar = view.findViewById(R.id.toolbar)
        // Set toolbar as ActionBar.
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

    protected abstract fun createNavigator(): Navigator

    protected abstract fun createAppBarLayout(): AppBarLayout

    protected abstract fun createContentLayout(): View

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private fun onDestroy() {
        navigationView.setNavigationItemSelectedListener(null)
        drawerLayout.removeDrawerListener(drawerToggle)

    }

    private fun Navigator.attach(view: NavigationView) {
        // Attach the Navigator.
        @IdRes val currentId = getCurrentItemId()
        // Restore the current selected id.
        view.menu?.findItem(currentId)?.isChecked = true

        view.setNavigationItemSelectedListener { item ->
            onItemSelected(item.itemId)
            // Close drawer after click.
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
        (this as? LifecycleObserver)?.let {
            activity.lifecycle.addObserver(it)
        }
    }
}