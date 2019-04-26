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

package com.fondesa.manganow.di

import android.app.Application
import com.fondesa.manganow.App
import com.fondesa.manganow.database.di.DatabaseModule
import com.fondesa.manganow.latest.di.LatestModule
import com.fondesa.manganow.log.di.LogModule
import com.fondesa.manganow.navigation.di.NavigationModule
import com.fondesa.manganow.remote.di.RemoteModule
import com.fondesa.manganow.serialization.di.SerializationModule
import com.fondesa.manganow.splash.di.SplashModule
import com.fondesa.manganow.thread.di.ThreadModule
import com.fondesa.manganow.time.di.TimeModule
import com.fondesa.manganow.ui.impl.UiModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        AppModule::class,
        ThreadModule::class,
        SerializationModule::class,
        NavigationModule::class,
        TimeModule::class,
        LogModule::class,
        RemoteModule::class,
        DatabaseModule::class,
        DatabaseTablesModule::class,
        UiModule::class,
        SplashModule::class,
        LatestModule::class
    ]
)
interface AppComponent : AndroidInjector<App> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
}