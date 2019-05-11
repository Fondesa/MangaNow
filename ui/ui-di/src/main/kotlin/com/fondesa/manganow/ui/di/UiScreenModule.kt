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

package com.fondesa.manganow.ui.di

import androidx.lifecycle.LifecycleObserver
import com.fondesa.manganow.ui.api.navigation.Navigator
import com.fondesa.manganow.ui.api.navigation.RouteNavigator
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoSet

@Module
interface UiScreenModule {

    @Binds
    fun provideNavigator(navigator: RouteNavigator): Navigator

    @Binds
    @IntoSet
    fun provideNavigatorLifecycleObserver(navigator: RouteNavigator): LifecycleObserver
}