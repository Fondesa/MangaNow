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

package com.fondesa.manganow.fragment

import android.content.res.Configuration
import android.os.Bundle
import android.support.annotation.IdRes
import android.support.annotation.LayoutRes
import android.support.v4.view.GravityCompat
import android.support.v4.view.ViewCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.*
import com.fondesa.manganow.R
import com.fondesa.manganow.navigation.Navigator
import com.fondesa.manganow.view.doOnLayout
import com.fondesa.manganow.view.getCheckedItem
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.partial_toolbar.*
import kotlinx.android.synthetic.main.screen_base_drawer.*
import javax.inject.Inject

abstract class DrawerFragment : DaggerFragment(),
    OnBackPressListener,
    AdditionalNavigationArgumentsProvider {

    @Inject
    lateinit var navigator: Navigator

    @Inject
    lateinit var drawerConfiguration: DrawerConfiguration

    @get:LayoutRes
    protected abstract val contentLayout: Int

    private lateinit var drawerToggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.screen_base_drawer, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        layoutInflater.inflate(contentLayout, screenContainer, true)

        navigationView.setNavigationItemSelectedListener { item ->
            @IdRes val selectedItemId = item.itemId
            @IdRes val currentItemId = navigationView.getCheckedItem()

            // Close drawer after click.
            drawerLayout.closeDrawer(Gravity.START)

            if (currentItemId == selectedItemId)
                return@setNavigationItemSelectedListener false

            val screen = drawerConfiguration.screenOfItem(selectedItemId)
            @IdRes val rootItemId = drawerConfiguration.rootItemId
            screen?.let {
                val strategy = when {
                    selectedItemId == rootItemId -> Navigator.Strategy.REPLACE_ALL
                    currentItemId == rootItemId -> Navigator.Strategy.NONE
                    else -> Navigator.Strategy.REPLACE_CURRENT
                }
                doOnDrawerClosed {
                    // Navigate to the next screen.
                    navigator.goTo(it, strategy)
                }
                return@setNavigationItemSelectedListener true
            }
            false
        }

        (activity as AppCompatActivity).setSupportActionBar(toolbar)

        drawerToggle = ActionBarDrawerToggle(
            activity,
            drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(drawerToggle)

        arguments?.getInt(ARG_DRAWER_ITEM_ID)?.let { drawerItemId ->
            navigationView.doOnLayout {
                // Restore the current selected item id.
                navigationView.setCheckedItem(drawerItemId)
            }
        }

        // Invalidate the window insets when the DrawerLayout is created.
        ViewCompat.requestApplyInsets(drawerLayout)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // Necessary after the initialization to synchronize the state of the DrawerLayout with the button.
        drawerToggle.syncState()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        drawerLayout.removeDrawerListener(drawerToggle)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        drawerToggle.onConfigurationChanged(newConfig)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return drawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item)
    }

    override fun onBackPressed(): Boolean {
        val drawerGravity = GravityCompat.START
        return if (drawerLayout.isDrawerOpen(drawerGravity)) {
            // Close the drawer if the drawer is opened.
            drawerLayout.closeDrawer(drawerGravity)
            true
        } else {
            false
        }
    }

    override fun provideAdditionalArguments(): Bundle =
        drawerItemBundle(navigationView.getCheckedItem())

    private inline fun doOnDrawerClosed(crossinline block: () -> Unit) {
        drawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerClosed(drawerView: View) {
                drawerLayout.removeDrawerListener(this)
                // Execute the block.
                block()
            }

            override fun onDrawerStateChanged(newState: Int) = Unit
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) = Unit
            override fun onDrawerOpened(drawerView: View) = Unit
        })
    }

    companion object {
        private const val ARG_DRAWER_ITEM_ID = "arg.drawerItemId"

        fun drawerItemBundle(@IdRes itemId: Int) = Bundle().apply {
            putInt(ARG_DRAWER_ITEM_ID, itemId)
        }
    }
}