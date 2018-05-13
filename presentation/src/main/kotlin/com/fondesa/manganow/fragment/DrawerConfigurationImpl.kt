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

package com.fondesa.manganow.fragment

import android.support.annotation.IdRes
import com.fondesa.manganow.R
import com.fondesa.manganow.navigation.Screen
import javax.inject.Inject

class DrawerConfigurationImpl @Inject constructor() : DrawerConfiguration {

    @get:IdRes
    override val rootItemId: Int = R.id.section_home

    override fun screenOfItem(@IdRes itemId: Int): Screen? = when (itemId) {
        R.id.section_home -> Screen.Latest()
        R.id.section_list -> Screen.MangaList()
        R.id.section_settings -> Screen.Settings()
        else -> null
    }
}