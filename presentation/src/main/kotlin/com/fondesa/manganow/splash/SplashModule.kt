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

import com.fondesa.data.category.repository.CategoryRepositoryImpl
import com.fondesa.data.category.storage.CategoryDiskStorageFactory
import com.fondesa.data.category.storage.CategoryRemoteStorageFactory
import com.fondesa.data.category.storage.disk.CategoryDiskStorageFactoryImpl
import com.fondesa.data.category.storage.remote.CategoryRemoteStorageFactoryImpl
import com.fondesa.data.sortorder.repository.SortOrderRepositoryImpl
import com.fondesa.data.sortorder.storage.SortOrderDiskStorageFactory
import com.fondesa.data.sortorder.storage.SortOrderRemoteStorageFactory
import com.fondesa.data.sortorder.storage.disk.SortOrderDiskStorageFactoryImpl
import com.fondesa.data.sortorder.storage.remote.SortOrderRemoteStorageFactoryImpl
import com.fondesa.domain.category.CategoryList
import com.fondesa.domain.category.repository.CategoryRepository
import com.fondesa.domain.category.usecase.GetCategoryList
import com.fondesa.domain.sortorder.SortOrderList
import com.fondesa.domain.sortorder.repository.SortOrderRepository
import com.fondesa.domain.sortorder.usecase.GetSortOrderList
import com.fondesa.domain.usecase.UseCase
import com.fondesa.manganow.converter.ConverterModule
import dagger.Binds
import dagger.Module

@Module(includes = [ConverterModule::class])
interface SplashModule {

    @Binds
    fun providePresenter(presenter: SplashPresenter): SplashContract.Presenter

    @Binds
    fun provideView(view: SplashFragment): SplashContract.View

    @Binds
    fun provideCategoryDiskStorageFactory(factory: CategoryDiskStorageFactoryImpl): CategoryDiskStorageFactory

    @Binds
    fun provideCategoryRemoteStorageFactory(factory: CategoryRemoteStorageFactoryImpl): CategoryRemoteStorageFactory

    @Binds
    fun provideCategoryRepository(repository: CategoryRepositoryImpl): CategoryRepository

    @Binds
    fun provideGetCategoryList(useCase: GetCategoryList): UseCase<CategoryList, Unit>

    @Binds
    fun provideSortOrderDiskStorageFactory(factory: SortOrderDiskStorageFactoryImpl): SortOrderDiskStorageFactory

    @Binds
    fun provideSortOrderRemoteStorageFactory(factory: SortOrderRemoteStorageFactoryImpl): SortOrderRemoteStorageFactory

    @Binds
    fun provideSortOrderRepository(repository: SortOrderRepositoryImpl): SortOrderRepository

    @Binds
    fun provideGetSortOrderList(useCase: GetSortOrderList): UseCase<SortOrderList, Unit>
}