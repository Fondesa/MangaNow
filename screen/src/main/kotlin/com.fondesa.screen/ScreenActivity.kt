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

package com.fondesa.screen

import android.support.annotation.IdRes
import android.support.v4.app.FragmentTransaction
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

abstract class ScreenActivity : DaggerAppCompatActivity(),
    ScreenManager {

    private val container by lazy { screenContainer() }

    @Inject
    lateinit var screenMap: ScreenMap

    protected val currentScreen: ScreenFragment?
        get() = supportFragmentManager.findFragmentById(container) as? ScreenFragment

    protected val currentDefinition: ScreenDefinition?
        get() = currentScreen?.let {
            screenMap.definitionOf(it::class)
        }

    @IdRes
    protected abstract fun screenContainer(): Int

    protected abstract fun rootScreenDefinition(): ScreenDefinition

    protected open fun onTransaction(
        transaction: FragmentTransaction,
        current: ScreenDefinition,
        next: ScreenDefinition
    ) = Unit

    protected open fun onScreenChange(
        current: ScreenDefinition,
        next: ScreenDefinition
    ) = Unit

    override fun navigateToRootScreen() {
        val rootDefinition = rootScreenDefinition()
        navigateToScreen(rootDefinition, true)
    }

    override fun navigateToScreen(
        definition: ScreenDefinition,
        addToStack: Boolean
    ) {
        val screenClass = screenMap.screenOf(definition)
        val screen = createScreen(screenClass)

        val transaction = supportFragmentManager.beginTransaction()
            .replace(container, screen)

        currentScreen?.let {
            val current = screenMap.definitionOf(it::class)
            onTransaction(transaction, current, definition)
            onScreenChange(current, definition)
            transaction.hide(it)
        }

        if (addToStack) {
            transaction.addToBackStack(screenClass.java.name)
        }

        transaction.commit()
    }

    override fun navigateToPreviousScreen() {
        val fm = supportFragmentManager
        val stackSize = fm.backStackEntryCount
        if (stackSize == 0) {
            finish()
            return
        }

        val lastEntry = fm.getBackStackEntryAt(stackSize - 1)
        @Suppress("UNCHECKED_CAST")
        val previousScreenClass = Class.forName(lastEntry.name).kotlin as ScreenClass

        val currentScreen = currentScreen!!
        if (stackSize == 1 && currentScreen::class == previousScreenClass) {
            finish()
            return
        }

        val currentDefinition = screenMap.definitionOf(currentScreen::class)
        val previousDefinition = screenMap.definitionOf(previousScreenClass)
        onScreenChange(currentDefinition, previousDefinition)
        fm.popBackStack()
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 0) {
            super.onBackPressed()
        } else {
            navigateToPreviousScreen()
        }
    }

    private fun createScreen(screenClass: ScreenClass): ScreenFragment {
        val constructor = screenClass.constructors.firstOrNull {
            it.parameters.isEmpty()
        } ?: throw IllegalArgumentException(
            "The class ${screenClass.java.name} must provide a public constructor with zero parameters."
        )

        return constructor.call()
    }
}
