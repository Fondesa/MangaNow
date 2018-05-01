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
import android.support.v4.app.FragmentTransaction
import com.fondesa.manganow.R
import com.fondesa.manganow.screen.Screens
import com.fondesa.screen.ScreenActivity
import com.fondesa.screen.ScreenDefinition

class HostActivity : ScreenActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_host)

        if (savedInstanceState == null) {
            // Navigate to the splash screen only the first time.
            navigateToScreen(Screens.SPLASH, false)
        } else if (currentDefinition != Screens.SPLASH) {
            applyDefaultConfiguration()
        }
    }

    override fun onTransaction(
        transaction: FragmentTransaction,
        current: ScreenDefinition,
        next: ScreenDefinition
    ) {
        if (current == Screens.SPLASH) {
            applyDefaultConfiguration()
        }
    }

    @IdRes
    override fun screenContainer(): Int = R.id.screenContainer

    private fun applyDefaultConfiguration() {
        // Change the window's background to the default color when the screen changes.
        window.setBackgroundDrawableResource(android.R.color.white)
    }
}