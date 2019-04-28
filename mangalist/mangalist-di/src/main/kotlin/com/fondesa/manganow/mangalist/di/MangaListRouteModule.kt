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

import com.fondesa.manganow.mangalist.api.MangaListRoute
import com.fondesa.manganow.mangalist.impl.MangaListRouteConsumer
import com.fondesa.manganow.navigation.api.Route
import com.fondesa.manganow.navigation.api.RouteConsumer
import com.fondesa.manganow.navigation.di.qualifiers.RouteKey
import com.fondesa.manganow.ui.api.navigation.NavigationSection
import com.fondesa.manganow.ui.di.NavigationSectionKey
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module(includes = [MangaListRouteModule.WithProvides::class])
interface MangaListRouteModule {

    @Binds
    @IntoMap
    @RouteKey(MangaListRoute::class)
    fun provideRouteConsumer(consumer: MangaListRouteConsumer): RouteConsumer

    @Module
    object WithProvides {

        @JvmStatic
        @Provides
        @IntoMap
        @NavigationSectionKey(NavigationSection.LIST)
        fun provideRouteForNavigationSection(): Route = MangaListRoute
    }
}