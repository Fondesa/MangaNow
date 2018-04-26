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

package com.fondesa.manganow.flow

import android.os.Bundle
import dagger.android.support.DaggerAppCompatActivity
import kotlin.reflect.KClass
import kotlin.reflect.jvm.isAccessible

abstract class ScreenActivity : DaggerAppCompatActivity(), ScreenManager {

    private val screenStack = mutableListOf<Screen>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val launchScreen = launchScreen()
        navigateToScreen(launchScreen)
    }

    override fun onScreenReady(tag: String) {
        TODO("not implemented")
    }

    override fun navigateToScreen(screenClass: KClass<out Screen>) {
        val screen = createScreen(screenClass)

        val transaction = supportFragmentManager.beginTransaction()

        val lastScreen = screenStack.lastOrNull()
        lastScreen?.let {
            transaction.hide(it)
        }

        transaction.add(screen, null)
            .commit()
    }

    override fun navigateToPreviousScreen() {
        if (screenStack.size <= 1) {
            finish()
            return
        }

        val previousScreen = screenStack[screenStack.size - 2]
        supportFragmentManager.beginTransaction()
            .remove(screenStack.last())
            .show(previousScreen)
            .commit()
    }

    override fun onBackPressed() {
        if (screenStack.size <= 1) {
            super.onBackPressed()
        } else {
            navigateToPreviousScreen()
        }
    }

    abstract fun launchScreen(): KClass<out Screen>

    private fun createScreen(screenClass: KClass<out Screen>): Screen {
        val constructor = screenClass.constructors.firstOrNull {
            it.parameters.isEmpty() && it.isAccessible
        } ?: throw IllegalArgumentException(
            "The class ${screenClass.java.name} must provide a public constructor with zero parameters."
        )

        return constructor.call()
    }
}
