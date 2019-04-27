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
import androidx.lifecycle.LifecycleObserver
import com.fondesa.manganow.latest.impl.*
import com.fondesa.manganow.latest.impl.qualifiers.PageSize
import com.fondesa.manganow.storage.api.remote.RemoteStorageConverter
import com.fondesa.manganow.ui.api.DefaultNavigationActivityViewDelegate
import com.fondesa.manganow.ui.api.NavigationActivityViewDelegate
import com.fondesa.manganow.ui.api.navigation.Navigator
import com.fondesa.manganow.ui.api.qualifiers.ScreenScope
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoSet

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

        @Binds
        fun provideOnLatestClickListener(activity: LatestActivity): LatestRecyclerViewAdapter.OnLatestClickListener

        @Binds
        fun provideView(activity: LatestActivity): LatestContract.View

        @Binds
        fun providePresenter(presenter: LatestPresenter): LatestContract.Presenter

        @Binds
        @IntoSet
        fun providePresenterLifecycleObserver(presenter: LatestPresenter): LifecycleObserver

        @Binds
        fun provideGetLatestList(getLatestList: GetLatestListImpl): GetLatestList

        @Binds
        fun provideLatestRemoteStorageConverter(factory: LatestRemoteStorageConverter): RemoteStorageConverter<LatestList>

        @Binds
        fun provideLatestRemoteStorageFactory(factory: LatestRemoteStorageFactoryImpl): LatestRemoteStorageFactory

        @Binds
        fun provideLatestDiskStorageFactory(factory: LatestDiskStorageFactoryImpl): LatestDiskStorageFactory
    }

    @Module
    object ScreenProvides {

        @JvmStatic
        @Provides
        @PageSize
        fun providePageSize(): Int = 25

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
    }
}