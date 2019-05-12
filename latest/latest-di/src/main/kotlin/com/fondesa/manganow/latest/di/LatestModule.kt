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
import com.fondesa.manganow.latest.api.LatestRoute
import com.fondesa.manganow.latest.impl.*
import com.fondesa.manganow.latest.impl.qualifiers.PageSize
import com.fondesa.manganow.navigation.api.Route
import com.fondesa.manganow.navigation.api.RouteConsumer
import com.fondesa.manganow.navigation.di.qualifiers.RouteKey
import com.fondesa.manganow.storage.api.remote.RemoteStorageMapper
import com.fondesa.manganow.ui.api.DefaultNavigationActivityViewDelegate
import com.fondesa.manganow.ui.api.NavigationActivityViewDelegate
import com.fondesa.manganow.ui.api.navigation.NavigationSection
import com.fondesa.manganow.ui.api.navigation.Navigator
import com.fondesa.manganow.ui.api.qualifiers.ScreenScope
import com.fondesa.manganow.ui.di.NavigationSectionKey
import com.fondesa.manganow.ui.di.UiScreenModule
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import dagger.multibindings.IntoSet

@Module(includes = [LatestModule.WithProvides::class])
interface LatestModule {

    @Binds
    @IntoMap
    @RouteKey(LatestRoute::class)
    fun provideRouteConsumer(consumer: LatestRouteConsumer): RouteConsumer

    @Binds
    fun provideGetLatestList(getLatestList: GetLatestListImpl): GetLatestList

    @Binds
    fun provideLatestRemoteStorageMapper(factory: LatestRemoteStorageMapper): RemoteStorageMapper<@JvmSuppressWildcards LatestList>

    @Binds
    fun provideLatestRemoteStorageFactory(factory: LatestRemoteStorageFactoryImpl): LatestRemoteStorageFactory

    @Binds
    fun provideLatestDiskStorageFactory(factory: LatestDiskStorageFactoryImpl): LatestDiskStorageFactory

    @Module
    object WithProvides {

        @JvmStatic
        @Provides
        @IntoMap
        @NavigationSectionKey(NavigationSection.HOME)
        fun provideRouteForNavigationSection(): Route = LatestRoute
    }

    @ScreenScope
    @ContributesAndroidInjector(
        modules = [
            ScreenBinds::class,
            ScreenProvides::class,
            UiScreenModule::class
        ]
    )
    fun latestActivity(): LatestActivity

    @Module
    interface ScreenBinds {

        @Binds
        fun provideActivity(activity: LatestActivity): Activity

        @Binds
        fun provideLatestAdapter(adapter: LatestRecyclerViewAdapterImpl): LatestRecyclerViewAdapter

        @Binds
        fun provideLatestHolderFactory(factory: LatestRecyclerViewHolderImplFactory): LatestRecyclerViewHolderFactory

        @Binds
        fun provideOnLatestClickListener(activity: LatestActivity): LatestRecyclerViewAdapter.OnLatestClickListener

        @Binds
        fun provideView(activity: LatestActivity): LatestContract.View

        @Binds
        fun providePresenter(presenter: LatestPresenter): LatestContract.Presenter

        @Binds
        @IntoSet
        fun providePresenterLifecycleObserver(presenter: LatestPresenter): LifecycleObserver
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