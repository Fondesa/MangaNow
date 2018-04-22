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

package com.fondesa.manganow.splash

import com.fondesa.data.cache.Cache
import com.fondesa.data.remote.loader.DefaultRemoteLoader
import com.fondesa.data.remote.loader.RemoteLoader
import com.fondesa.data.serialization.FromJsonConverter
import com.fondesa.data.sortorder.cache.SortOrderCache
import com.fondesa.data.sortorder.converter.SortOrderJsonConverter
import com.fondesa.data.sortorder.repository.DefaultSortOrderRepository
import com.fondesa.domain.sortorder.SortOrderList
import com.fondesa.domain.sortorder.repository.SortOrderRepository
import com.fondesa.domain.sortorder.usecase.GetSortOrderList
import com.fondesa.domain.usecase.UseCase
import com.fondesa.manganow.converter.ConverterModule
import com.fondesa.manganow.time.TimeModule
import dagger.Binds
import dagger.Module

@Module(includes = [TimeModule::class, ConverterModule::class])
interface SplashModule {

    @Binds
    fun providePresenter(presenter: SplashPresenter): SplashContract.Presenter

    @Binds
    fun provideView(view: SplashActivity): SplashContract.View

    @Binds
    fun provideSortOrderJsonConverter(converter: SortOrderJsonConverter): FromJsonConverter<SortOrderList>

    @Binds
    fun provideSortOrderRemoteLoader(loader: @JvmSuppressWildcards DefaultRemoteLoader<SortOrderList>): RemoteLoader<SortOrderList>

    @Binds
    fun provideSortOrderCache(cache: SortOrderCache): Cache<SortOrderList>

    @Binds
    fun provideSortOrderRepository(repository: DefaultSortOrderRepository): SortOrderRepository

    @Binds
    fun provideGetSortOrderList(useCase: GetSortOrderList): UseCase<SortOrderList, Unit>
}