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

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.annotation.IdRes
import android.support.v4.app.FragmentTransaction
import dagger.android.support.DaggerAppCompatActivity
import java.util.*

abstract class ScreenActivity : DaggerAppCompatActivity(),
    ScreenManager {

    protected val currentKey: ScreenKey?
        get() = keys.lastOrNull()

    private val container by lazy { screenContainer() }
    private val keys = LinkedList<ScreenKey>()

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
        screenArgs.putParcelable(ScreenFragment.ARG_SCREEN_KEY, nextKey)
        nextScreen.arguments = screenArgs

        val transaction = beginTransaction()
            .add(container, nextScreen, nextKey.tag)

        val currentScreen = keys.lastOrNull()?.getFragmentOrNull()
        val currentKey = currentScreen?.key

        if (strategy == ScreenManager.StackStrategy.REPLACE_ALL) {
            keys.mapNotNull {
                it.getFragmentOrNull()
            }.forEach {
                transaction.remove(it)
            }
            keys.clear()
        } else if (currentScreen != null) {
            if (strategy == ScreenManager.StackStrategy.REPLACE_CURRENT) {
                transaction.remove(currentScreen)
                keys.removeLast()
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
        keys.add(nextKey)
    }

    override fun onDestroy() {
        super.onDestroy()
        keys.clear()
    }

    override fun navigateToPreviousScreen() {
        val stackSize = keys.size
        if (stackSize <= 1) {
            finish()
            return
        }

        val currentScreen = keys.pollLast().getFragment()
        val previousScreen = keys.last.getFragment()

        val currentKey = currentScreen.key
        val previousKey = previousScreen.key
        onScreenChange(currentKey, previousKey)

        beginTransaction()
            .remove(currentScreen)
            .attach(previousScreen)
            .commit()
    }

    override fun onBackPressed() {
        if (keys.size <= 1) {
            super.onBackPressed()
        } else {
            navigateToPreviousScreen()
        }
    }

    @SuppressLint("CommitTransaction")
    private fun beginTransaction() = supportFragmentManager.beginTransaction()
        .disallowAddToBackStack()

    private fun ScreenKey.getFragment() = getFragmentOrNull()
            ?: throw NullPointerException("Cannot find a ${ScreenFragment::class.java.name} for the tag $tag")

    private fun ScreenKey.getFragmentOrNull() =
        supportFragmentManager.findFragmentByTag(tag) as? ScreenFragment
}