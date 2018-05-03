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

import android.os.Bundle
import android.support.annotation.IdRes
import android.support.v4.app.FragmentTransaction
import dagger.android.support.DaggerAppCompatActivity
import java.util.*

abstract class ScreenActivity : DaggerAppCompatActivity(),
    ScreenManager {

    private val container by lazy { screenContainer() }

    protected val currentKey: ScreenKey?
        get() = stack.lastOrNull()?.key

    private val stack = LinkedList<ScreenFragment>()

    @IdRes
    protected abstract fun screenContainer(): Int

    protected open fun onTransaction(
        transaction: FragmentTransaction,
        current: ScreenKey,
        next: ScreenKey
    ) = Unit

    protected open fun onScreenChange(
        current: ScreenKey,
        next: ScreenKey
    ) = Unit

    override fun navigateToScreen(
        configuration: ScreenConfiguration,
        strategy: ScreenManager.StackStrategy
    ) {
        val nextScreen = configuration.createScreen()
        val nextKey = configuration.key

        val screenArgs = nextScreen.arguments ?: Bundle()
        screenArgs. putParcelable(ScreenFragment.ARG_SCREEN_KEY, nextKey)
        nextScreen.arguments = screenArgs

        val transaction = supportFragmentManager.beginTransaction()
            .add(container, nextScreen)

        val currentScreen = stack.lastOrNull()
        val currentKey = currentScreen?.key

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

        currentKey?.also {
            onTransaction(transaction, it, nextKey)
            onScreenChange(it, nextKey)
        }

        transaction.commit()
        // Add the screen to the stack.
        stack.add(nextScreen)
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

        val currentKey = currentScreen.key
        val previousKey = previousScreen.key
        onScreenChange(currentKey, previousKey)

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
}