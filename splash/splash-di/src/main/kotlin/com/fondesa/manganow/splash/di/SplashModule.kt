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

package com.fondesa.manganow.splash.di

import android.app.Activity
import androidx.lifecycle.LifecycleObserver
import com.fondesa.manganow.splash.impl.R
import com.fondesa.manganow.splash.impl.SplashActivity
import com.fondesa.manganow.splash.impl.SplashContract
import com.fondesa.manganow.splash.impl.SplashPresenter
import com.fondesa.manganow.splash.impl.category.*
import com.fondesa.manganow.storage.api.remote.RemoteStorageMapper
import com.fondesa.manganow.ui.api.FullScreenActivityViewDelegate
import com.fondesa.manganow.ui.api.FullScreenActivityViewDelegateImpl
import com.fondesa.manganow.ui.api.qualifiers.ScreenScope
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoSet

@Module
interface SplashModule {

    @Binds
    fun provideGetCategoryList(getCategoryList: GetCategoryListImpl): GetCategoryList

    @Binds
    fun provideCategoryRemoteStorageMapper(factory: CategoryRemoteStorageMapper): RemoteStorageMapper<@JvmSuppressWildcards CategoryList>

    @Binds
    fun provideCategoryRemoteStorageFactory(factory: CategoryRemoteStorageFactoryImpl): CategoryRemoteStorageFactory

    @Binds
    fun provideCategoryDiskStorageFactory(factory: CategoryDiskStorageFactoryImpl): CategoryDiskStorageFactory

    @ScreenScope
    @ContributesAndroidInjector(
        modules = [
            ScreenBinds::class,
            ScreenProvides::class
        ]
    )
    fun splashActivity(): SplashActivity

    @Module
    interface ScreenBinds {

        @Binds
        fun provideActivity(activity: SplashActivity): Activity

        @Binds
        fun provideView(activity: SplashActivity): SplashContract.View

        @Binds
        fun providePresenter(presenter: SplashPresenter): SplashContract.Presenter

        @Binds
        @IntoSet
        fun providePresenterLifecycleObserver(presenter: SplashPresenter): LifecycleObserver
    }

    @Module
    object ScreenProvides {

        @JvmStatic
        @Provides
        fun provideActivityViewDelegate(activity: SplashActivity): FullScreenActivityViewDelegate =
            FullScreenActivityViewDelegateImpl(
                activity = activity,
                contentLayout = R.layout.activity_splash,
                fitsSystemWindows = false
            )
    }
}