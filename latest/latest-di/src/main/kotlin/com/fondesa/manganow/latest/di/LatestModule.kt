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

package com.fondesa.manganow.latest.di

import android.app.Activity
import com.fondesa.manganow.latest.impl.LatestActivity
import com.fondesa.manganow.latest.impl.LatestRecyclerViewAdapter
import com.fondesa.manganow.latest.impl.LatestRecyclerViewHolderFactory
import com.fondesa.manganow.ui.api.DefaultNavigationActivityViewDelegate
import com.fondesa.manganow.ui.api.NavigationActivityViewDelegate
import com.fondesa.manganow.ui.api.navigation.Navigator
import com.fondesa.manganow.ui.api.qualifiers.ScreenScope
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector

@Module
interface LatestModule {

    @ScreenScope
    @ContributesAndroidInjector(
        modules = [
            ScreenBinds::class,
            ScreenProvides::class,
            LatestRouteModule::class
        ]
    )
    fun latestActivity(): LatestActivity

    @Module
    interface ScreenBinds {

        @Binds
        fun provideActivity(activity: LatestActivity): Activity
    }

    @Module
    object ScreenProvides {

        @JvmStatic
        @Provides
        fun provideActivityViewDelegate(
            activity: LatestActivity,
            navigator: Navigator
        ): NavigationActivityViewDelegate = DefaultNavigationActivityViewDelegate(
            activity = activity,
            navigator = navigator,
            contentLayout = R.layout.activity_latest
        )

        @JvmStatic
        @Provides
        fun provideAdapter(
            activity: LatestActivity,
            factory: LatestRecyclerViewHolderFactory
        ): LatestRecyclerViewAdapter = LatestRecyclerViewAdapter(
            pageSize = 13 /*TODO*/,
            holderFactory = factory,
            clickListener = activity
        )

        @JvmStatic
        @Provides
        fun provideHolderFactory(): LatestRecyclerViewHolderFactory =
            LatestRecyclerViewHolderFactory()
    }
}