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

package com.fondesa.manganow.ui.api.navigation

import android.app.Activity
import android.content.Context
import androidx.annotation.IdRes

/**
 * Used to define the behavior to navigate in the main sections of the app.
 * The [Navigator] must be attached to an [Activity] in order to be used.
 */
interface Navigator {

    /**
     * Attaches an [Activity] instance and initialize the [Context] resources.
     *
     * @param activity instance of [Activity] attached to the [Navigator].
     */
    fun attach(activity: Activity)

    /**
     * Detaches the [Activity] instance and release the [Context] resources.
     */
    fun detach()

    /**
     * Called when a new item is selected to start the navigation to that item.
     *
     * @param selectedId id of the selected item.
     */
    fun onItemSelected(@IdRes selectedId: Int)

    /**
     * Get the current selected item id.
     * This can be useful to restore the state of the navigation in a new section.
     *
     * @return current selected id.
     */
    @IdRes
    fun getCurrentItemId(): Int
}