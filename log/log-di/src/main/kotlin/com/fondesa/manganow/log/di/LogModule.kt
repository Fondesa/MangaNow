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

package com.fondesa.manganow.log.di

import com.fondesa.manganow.core.lifecycle.AppInitializer
import com.fondesa.manganow.log.api.Logger
import com.fondesa.manganow.log.impl.TimberInitializer
import com.fondesa.manganow.log.impl.TimberLogger
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoSet

@Module
interface LogModule {

    @Binds
    fun provideLogger(logger: TimberLogger): Logger

    @Binds
    @IntoSet
    fun provideTimberInitializer(initializer: TimberInitializer): AppInitializer
}