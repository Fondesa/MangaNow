/*
 * Copyright (c) 2019 Fondesa
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

package com.fondesa.manganow.ui.screen.navigation

import android.content.Context
import android.content.Intent
import androidx.annotation.IdRes

/**
 * Default implementation of [IntentNavigator] that specifies the [Intent] that will be launched
 * when an item is clicked and the default main section.
 */
class DefaultIntentNavigator : IntentNavigator() {

    @IdRes
    override fun startNavigationId(): Int = TODO()

    override fun intentOf(context: Context, @IdRes itemId: Int) = when (itemId) {
        else -> throw IllegalArgumentException("Unsupported item id.")
    }
}

interface Router {


}