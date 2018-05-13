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

package com.fondesa.manganow.injection.application

import android.app.Application
import android.content.Context
import com.fondesa.manganow.injection.database.DatabaseModule
import com.fondesa.manganow.injection.remote.RemoteModule
import com.fondesa.manganow.injection.screen.ScreenModule
import com.fondesa.manganow.injection.thread.ThreadModule
import com.fondesa.manganow.injection.time.TimeModule
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [RemoteModule::class,
    DatabaseModule::class,
    ThreadModule::class,
    TimeModule::class,
    ScreenModule::class])
class AppModule {

    @Singleton
    @Provides
    fun provideContext(application: Application): Context = application

    @Singleton
    @Provides
    fun provideGson(): Gson = Gson()
}