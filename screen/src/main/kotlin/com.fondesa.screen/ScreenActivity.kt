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
import java.util.*
import javax.inject.Inject

abstract class ScreenActivity : DaggerAppCompatActivity(),
    ScreenManager {

    private val container by lazy { screenContainer() }

    @Inject
    lateinit var screenMap: ScreenMap

    protected val currentDefinition: ScreenDefinition?
        get() = stack.lastOrNull()?.let {
            screenMap.definitionOf(it::class)
        }

    private val stack = LinkedList<ScreenFragment>()

    @IdRes
    protected abstract fun screenContainer(): Int

    protected open fun onTransaction(
        transaction: FragmentTransaction,
        current: ScreenDefinition,
        next: ScreenDefinition
    ) = Unit

    protected open fun onScreenChange(
        current: ScreenDefinition,
        next: ScreenDefinition
    ) = Unit

    override fun navigateToScreen(
        definition: ScreenDefinition,
        strategy: ScreenManager.StackStrategy
    ) {
        val screenClass = screenMap.screenOf(definition)
        val screen = createScreen(screenClass)

        val transaction = supportFragmentManager.beginTransaction()
            .add(container, screen)

        val currentScreen = stack.lastOrNull()
        val current = currentScreen?.let {
            screenMap.definitionOf(it::class)
        }
        if (strategy == ScreenManager.StackStrategy.REPLACE_ALL) {
            stack.forEach {
                transaction.remove(it)
            }
            stack.clear()
        } else if (currentScreen != null) {
            if (strategy == ScreenManager.StackStrategy.REPLACE_CURRENT) {
                transaction.remove(currentScreen)
                stack.removeLast()
            } else {
                transaction.detach(currentScreen)
            }
        }

        current?.also {
            onTransaction(transaction, it, definition)
            onScreenChange(it, definition)
        }

        transaction.commit()
        // Add the screen to the stack.
        stack.add(screen)
    }

    override fun onDestroy() {
        super.onDestroy()
        stack.clear()
    }

    override fun navigateToPreviousScreen() {
        val fm = supportFragmentManager
        val stackSize = stack.size
        if (stackSize <= 1) {
            finish()
            return
        }

        val currentScreen = stack.pollLast()
        val previousScreen = stack.last

        val currentDefinition = screenMap.definitionOf(currentScreen::class)
        val previousDefinition = screenMap.definitionOf(previousScreen::class)
        onScreenChange(currentDefinition, previousDefinition)

        fm.beginTransaction()
            .remove(currentScreen)
            .attach(previousScreen)
            .commit()
    }

    override fun onBackPressed() {
        if (stack.size <= 1) {
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
