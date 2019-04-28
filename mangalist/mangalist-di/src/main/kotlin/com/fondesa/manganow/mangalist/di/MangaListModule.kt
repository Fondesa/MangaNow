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

package com.fondesa.manganow.mangalist.di

import android.app.Activity
import androidx.lifecycle.LifecycleObserver
import com.fondesa.manganow.latest.di.LatestRouteModule
import com.fondesa.manganow.mangalist.api.sortorder.GetSortOrderList
import com.fondesa.manganow.mangalist.api.sortorder.SortOrderList
import com.fondesa.manganow.mangalist.impl.*
import com.fondesa.manganow.mangalist.impl.qualifiers.PageSize
import com.fondesa.manganow.mangalist.impl.sortorder.*
import com.fondesa.manganow.storage.api.remote.RemoteStorageMapper
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
interface MangaListModule {

    @Binds
    fun provideGetSortOrderList(getSortOrderList: GetSortOrderListImpl): GetSortOrderList

    @Binds
    fun provideSortOrderRemoteStorageMapper(factory: SortOrderRemoteStorageMapper): RemoteStorageMapper<@JvmSuppressWildcards SortOrderList>

    @Binds
    fun provideSortOrderRemoteStorageFactory(factory: SortOrderRemoteStorageFactoryImpl): SortOrderRemoteStorageFactory

    @Binds
    fun provideSortOrderDiskStorageFactory(factory: SortOrderDiskStorageFactoryImpl): SortOrderDiskStorageFactory

    @Binds
    fun provideGetMangaList(getMangaList: GetMangaList): GetMangaList

    @Binds
    fun provideMangaListRemoteStorageMapper(factory: MangaListRemoteStorageMapper): RemoteStorageMapper<@JvmSuppressWildcards MangaList>

    @Binds
    fun provideMangaListRemoteStorageFactory(factory: MangaListRemoteStorageFactoryImpl): MangaListRemoteStorageFactory

    @Binds
    fun provideMangaListDiskStorageFactory(factory: MangaListDiskStorageFactoryImpl): MangaListDiskStorageFactory

    @ScreenScope
    @ContributesAndroidInjector(
        modules = [
            ScreenBinds::class,
            ScreenProvides::class,
            LatestRouteModule::class,
            MangaListRouteModule::class
        ]
    )
    fun mangaListActivity(): MangaListActivity

    @Module
    interface ScreenBinds {

        @Binds
        fun provideActivity(activity: MangaListActivity): Activity

        @Binds
        fun provideOnMangaClickListener(activity: MangaListActivity): MangaListRecyclerViewAdapter.OnMangaClickListener

        @Binds
        fun provideView(activity: MangaListActivity): MangaListContract.View

        @Binds
        fun providePresenter(presenter: MangaListPresenter): MangaListContract.Presenter

        @Binds
        @IntoSet
        fun providePresenterLifecycleObserver(presenter: MangaListPresenter): LifecycleObserver
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
            activity: MangaListActivity,
            navigator: Navigator
        ): NavigationActivityViewDelegate = DefaultNavigationActivityViewDelegate(
            activity = activity,
            navigator = navigator,
            contentLayout = R.layout.activity_manga_list
        )
    }
}