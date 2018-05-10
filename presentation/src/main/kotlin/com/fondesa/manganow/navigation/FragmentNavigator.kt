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

package com.fondesa.manganow.navigation

import android.annotation.SuppressLint
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import com.fondesa.manganow.latest.LatestScreen
import com.fondesa.manganow.manga.list.MangaListScreen
import com.fondesa.manganow.screen.Screen
import com.fondesa.manganow.settings.SettingsScreen
import com.fondesa.manganow.splash.SplashScreen
import com.fondesa.screen.ScreenFragment
import com.fondesa.screen.ScreenManager
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.util.*

class FragmentNavigator(private val fragmentManager: FragmentManager) : Navigator {

    private val screens = LinkedList<Screen>()

    override fun goTo(screen: Screen, strategy: Navigator.Strategy) {
        val nextFragment = mapScreenToFragment(screen)

        val transaction = beginTransaction()
            .add(android.R.id.content, nextFragment, screen.tag)

        val currentScreen = screens.lastOrNull()?.getFragmentOrNull()

        if (strategy == ScreenManager.StackStrategy.REPLACE_ALL) {
            screens.mapNotNull {
                it.getFragmentOrNull()
            }.forEach {
                transaction.remove(it)
            }
            screens.clear()
        } else if (currentScreen != null) {
            if (strategy == ScreenManager.StackStrategy.REPLACE_CURRENT) {
                transaction.remove(currentScreen)
                screens.removeLast()
            } else {
                transaction.detach(currentScreen)
            }
        }

        transaction.commit()
        // Add the screen to the stack.
        screens.add(screen)
    }

    override fun goBack() {
        if (!canGoBack()) {
            throw IllegalStateException("Can't navigate back.")
        }

        val currentScreen = screens.pollLast().getFragment()
        val previousScreen = screens.last.getFragment()

        beginTransaction()
            .remove(currentScreen)
            .attach(previousScreen)
            .commit()
    }

    override fun canGoBack(): Boolean = screens.size > 1

    override fun saveState(): ByteArray {
        val bos = ByteArrayOutputStream()
        val output = ObjectOutputStream(bos)
        output.writeObject(screens)
        output.flush()
        val state = bos.toByteArray()
        bos.close()
        return state
    }

    override fun restoreState(state: ByteArray) {
        val bis = ByteArrayInputStream(state)
        val input = ObjectInputStream(bis)
        screens.clear()
        @Suppress("UNCHECKED_CAST")
        screens.addAll(input.readObject() as Collection<Screen>)
        input.close()
    }

    @SuppressLint("CommitTransaction")
    private fun beginTransaction() = fragmentManager.beginTransaction()
        .disallowAddToBackStack()

    private fun mapScreenToFragment(screen: Screen): Fragment = when (screen) {
        is Screen.Splash -> SplashScreen()
        is Screen.Latest -> LatestScreen()
        is Screen.MangaList -> MangaListScreen()
        is Screen.Settings -> SettingsScreen()
    }

    private fun Screen.getFragment() = getFragmentOrNull()
            ?: throw NullPointerException("Cannot find a ${ScreenFragment::class.java.name} for the tag $tag")

    private fun Screen.getFragmentOrNull() =
        fragmentManager.findFragmentByTag(tag) as? ScreenFragment

    private val Screen.tag: String
        get() = this::class.java.name
}