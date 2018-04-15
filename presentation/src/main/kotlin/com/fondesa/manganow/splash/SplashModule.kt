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

import com.fondesa.data.sortorder.repository.DefaultSortOrderRepository
import com.fondesa.data.sortorder.store.SortOrderCacheDataStore
import com.fondesa.data.sortorder.store.SortOrderRemoteDataStore
import com.fondesa.data.store.CacheDataStore
import com.fondesa.data.store.RemoteDataStore
import com.fondesa.domain.sortorder.model.SortOrder
import com.fondesa.domain.sortorder.repository.SortOrderRepository
import com.fondesa.domain.sortorder.usecase.GetSortOrderList
import com.fondesa.domain.usecase.UseCase
import com.fondesa.manganow.converter.ConverterModule
import com.fondesa.manganow.time.TimeModule
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module(includes = [SplashModule.UseCases::class, TimeModule::class, ConverterModule::class])
interface SplashModule {

    @Binds
    fun providePresenter(presenter: SplashPresenter): SplashContract.Presenter

    @Binds
    fun provideView(view: SplashActivity): SplashContract.View

    @Binds
    fun provideSortOrderRepository(repository: DefaultSortOrderRepository): SortOrderRepository

    @Module
    class UseCases {

        //TODO: probably remove this checking: https://github.com/google/dagger/issues/1143

        @Provides
        fun provideGetSortOrderList(useCase: GetSortOrderList): UseCase<List<SortOrder>, Unit> =
            useCase

        @Provides
        fun provideSortOrderCacheDataStore(store: SortOrderCacheDataStore): CacheDataStore<List<SortOrder>> =
            store

        @Provides
        fun provideSortOrderRemoteDataStore(store: SortOrderRemoteDataStore): RemoteDataStore<List<SortOrder>> =
            store
    }
}