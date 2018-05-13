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

package com.fondesa.manganow.injection.screen

import com.fondesa.manganow.fragment.DrawerModule
import com.fondesa.manganow.injection.latest.LatestModule
import com.fondesa.manganow.injection.manga.list.MangaListModule
import com.fondesa.manganow.injection.splash.SplashModule
import com.fondesa.manganow.latest.LatestFragment
import com.fondesa.manganow.manga.list.MangaListFragment
import com.fondesa.manganow.navigation.FragmentNavigator
import com.fondesa.manganow.navigation.Navigator
import com.fondesa.manganow.navigation.ScreenManager
import com.fondesa.manganow.settings.SettingsFragment
import com.fondesa.manganow.splash.SplashFragment
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector

@Module
interface ScreenModule {

    @ScreenManagerScope
    @ContributesAndroidInjector(modules = [Navigation::class, Fragments::class])
    fun provideScreenManager(): ScreenManager

    @Module
    class Navigation {

        @ScreenManagerScope
        @Provides
        fun provideNavigator(manager: ScreenManager): Navigator =
            FragmentNavigator(manager.supportFragmentManager)
    }

    @Module
    interface Fragments {

        @ScreenScope
        @ContributesAndroidInjector(modules = [SplashModule::class])
        fun provideSplashScreen(): SplashFragment

        @ScreenScope
        @ContributesAndroidInjector(modules = [DrawerModule::class, LatestModule::class])
        fun provideLatestScreen(): LatestFragment

        @ScreenScope
        @ContributesAndroidInjector(modules = [DrawerModule::class, MangaListModule::class])
        fun provideMangaListScreen(): MangaListFragment

        @ScreenScope
        @ContributesAndroidInjector(modules = [DrawerModule::class])
        fun provideSettingsScreen(): SettingsFragment
    }
}