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
import android.support.annotation.IdRes
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.Gravity
import android.view.View
import com.fondesa.manganow.R
import com.fondesa.manganow.controller.TitleController
import com.fondesa.manganow.extension.getCheckedItem
import com.fondesa.manganow.navigation.Navigation
import com.fondesa.manganow.screen.ScreenConfigurations
import com.fondesa.manganow.screen.ScreenKeys
import com.fondesa.screen.ScreenActivity
import com.fondesa.screen.ScreenKey
import com.fondesa.screen.ScreenManager
import kotlinx.android.synthetic.main.activity_host.*
import kotlinx.android.synthetic.main.partial_toolbar.*
import javax.inject.Inject

class HostActivity : ScreenActivity(), TitleController {

    @Inject
    lateinit var navigation: Navigation

    private val drawerToggle: ActionBarDrawerToggle by lazy {
        ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
    }

    private val rootItemId by lazy { navigation.rootItemId() }

    private val rootKey by lazy {
        val configuration = navigation.configurationOfItem(rootItemId)
                ?: throw NullPointerException("The root item id should be associated with a configuration")
        configuration.key
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_host)

        navigationView.setNavigationItemSelectedListener { item ->
            @IdRes val selectedItemId = item.itemId
            @IdRes val currentItemId = navigationView.getCheckedItem()

            // Close drawer after click.
            drawerLayout.closeDrawer(Gravity.START)

            if (currentItemId == selectedItemId)
                return@setNavigationItemSelectedListener false

            val screenConfiguration = navigation.configurationOfItem(selectedItemId)
            screenConfiguration?.let {
                val strategy = when {
                    selectedItemId == rootItemId -> ScreenManager.StackStrategy.REPLACE_ALL
                    currentItemId == rootItemId -> ScreenManager.StackStrategy.NONE
                    else -> ScreenManager.StackStrategy.REPLACE_CURRENT
                }
                // Navigate to the next screen.
                navigateToScreen(it, strategy)
                return@setNavigationItemSelectedListener true
            }
            false
        }

        if (savedInstanceState == null) {
            // Navigate to the splash screen only the first time.
            navigateToScreen(ScreenConfigurations.SPLASH)
        } else if (currentKey != ScreenKeys.SPLASH) {
            applyDefaultConfiguration()
        }
    }

    @IdRes
    override fun screenContainer(): Int = R.id.screenContainer

    override fun onScreenChange(current: ScreenKey, next: ScreenKey) {
        if (current == ScreenKeys.SPLASH) {
            applyDefaultConfiguration()
        }

        if (next == rootKey) {
            navigationView.setCheckedItem(rootItemId)
        }
    }

    override fun changeTitle(title: String) {
        this.title = title
    }

    override fun onDestroy() {
        super.onDestroy()
        drawerLayout.removeDrawerListener(drawerToggle)
    }

    override fun onBackPressed() {
        val drawerGravity = GravityCompat.START
        if (drawerLayout.isDrawerOpen(drawerGravity)) {
            // Close the drawer if the drawer is opened.
            drawerLayout.closeDrawer(drawerGravity)
        } else {
            super.onBackPressed()
        }
    }

    private fun applyDefaultConfiguration() {
        // Change the window's background to the default color when the screen changes.
        window.setBackgroundDrawableResource(android.R.color.white)
        // Show the AppBarLayout.
        appBarLayout.visibility = View.VISIBLE

        // The title is set to null to not show the change to the user.
        title = null
        setSupportActionBar(toolbar)

        drawerLayout.addDrawerListener(drawerToggle)
        // Necessary after the initialization to synchronize the state of the DrawerLayout with the button.
        drawerToggle.syncState()
    }
}