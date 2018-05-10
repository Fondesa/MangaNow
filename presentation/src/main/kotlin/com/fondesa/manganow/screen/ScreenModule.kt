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

import com.fondesa.manganow.activity.HostActivity
import com.fondesa.manganow.activity.NavigationActivity
import com.fondesa.manganow.latest.LatestScreen
import com.fondesa.manganow.manga.list.MangaListModule
import com.fondesa.manganow.manga.list.MangaListScreen
import com.fondesa.manganow.navigation.NavigationModule
import com.fondesa.manganow.settings.SettingsScreen
import com.fondesa.manganow.splash.SplashModule
import com.fondesa.manganow.splash.SplashScreen
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface ScreenModule {

    @ContributesAndroidInjector(modules = [NavigationModule::class])
    fun provideNavigationActivity(): NavigationActivity

    @ContributesAndroidInjector(modules = [NavigationModule::class])
    fun provideHostActivity(): HostActivity

    @ContributesAndroidInjector(modules = [SplashModule::class])
    fun provideSplashScreen(): SplashScreen

    @ContributesAndroidInjector
    fun provideLatestScreen(): LatestScreen

    @ContributesAndroidInjector(modules = [MangaListModule::class])
    fun provideMangaListScreen(): MangaListScreen

    @ContributesAndroidInjector
    fun provideSettingsScreen(): SettingsScreen
}