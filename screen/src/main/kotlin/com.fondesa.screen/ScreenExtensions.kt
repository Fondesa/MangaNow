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
import kotlin.reflect.full.primaryConstructor

inline fun <reified T : ScreenFragment> screenConfiguration(
    key: ScreenKey,
    crossinline argsBlock: Bundle.() -> Unit = {}
): ScreenConfiguration =
    object : ScreenConfiguration {
        override val key: ScreenKey = key

        override fun createScreen(): ScreenFragment {
            val constructor = T::class.primaryConstructor
                    ?: throw IllegalArgumentException("You have to provide an empty primary constructor")

            val fragment = constructor.call()
            val args = Bundle()
            argsBlock(args)
            fragment.arguments = args
            return fragment
        }
    }

inline fun <reified T : ScreenFragment> ScreenManager.navigateToScreen(
    key: ScreenKey,
    strategy: ScreenManager.StackStrategy = ScreenManager.StackStrategy.NONE,
    crossinline argsBlock: Bundle.() -> Unit = {}
) {
    val configuration = screenConfiguration<T>(key, argsBlock)
    navigateToScreen(configuration, strategy)
}