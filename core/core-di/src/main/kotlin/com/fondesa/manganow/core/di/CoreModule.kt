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

package com.fondesa.manganow.core.di

import com.fondesa.manganow.core.api.ActivityRetriever
import com.fondesa.manganow.core.api.AppInitializer
import com.fondesa.manganow.core.impl.ActivityRetrieverImpl
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoSet

@Module
interface CoreModule {

    @Binds
    @IntoSet
    fun provideActivityRetrieverInitializer(retriever: ActivityRetrieverImpl): AppInitializer

    @Binds
    fun provideActivityRetriever(retriever: ActivityRetrieverImpl): ActivityRetriever
}