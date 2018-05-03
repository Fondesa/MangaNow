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

package com.fondesa.manganow.screen

import com.fondesa.manganow.latest.LatestScreen
import com.fondesa.manganow.manga.list.MangaListScreen
import com.fondesa.manganow.settings.SettingsScreen
import com.fondesa.manganow.splash.SplashScreen
import com.fondesa.screen.ScreenConfiguration
import com.fondesa.screen.ScreenFragment
import com.fondesa.screen.ScreenKey

enum class ScreenConfigurations(
    override val key: ScreenKey,
    private inline val screenBlock: () -> ScreenFragment
) : ScreenConfiguration {

    SPLASH(ScreenKeys.SPLASH, { SplashScreen() }),

    LATEST(ScreenKeys.LATEST, { LatestScreen() }),

    MANGA_LIST(ScreenKeys.MANGA_LIST, { MangaListScreen() }),

    SETTINGS(ScreenKeys.SETTINGS, { SettingsScreen() });

    override fun createScreen(): ScreenFragment = screenBlock()
}