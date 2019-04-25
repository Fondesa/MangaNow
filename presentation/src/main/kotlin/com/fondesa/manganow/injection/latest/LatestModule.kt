///*
// * Copyright (c) 2018 Fondesa
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *     http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package com.fondesa.manganow.injection.latest
//
//import com.fondesa.data.latest.repository.LatestRepositoryImpl
//import com.fondesa.data.latest.storage.LatestDiskStorageFactory
//import com.fondesa.data.latest.storage.LatestRemoteStorageFactory
//import com.fondesa.data.latest.storage.disk.LatestDiskStorageFactoryImpl
//import com.fondesa.data.latest.storage.remote.LatestRemoteStorageFactoryImpl
//import com.fondesa.domain.latest.repository.LatestRepository
//import com.fondesa.domain.latest.usecase.GetLatestList
//import com.fondesa.domain.latest.usecase.GetLatestListImpl
//import com.fondesa.manganow.latest.LatestContract
//import com.fondesa.manganow.latest.LatestFragment
//import com.fondesa.manganow.latest.LatestPresenter
//import dagger.Binds
//
////@Module(includes = [ConverterModule::class])
//interface LatestModule {
//
//    @Binds
//    fun providePresenter(presenter: LatestPresenter): LatestContract.Presenter
//
//    @Binds
//    fun provideView(view: LatestFragment): LatestContract.View
//
//    @Binds
//    fun provideDiskStorageFactory(factory: LatestDiskStorageFactoryImpl): LatestDiskStorageFactory
//
//    @Binds
//    fun provideRemoteStorageFactory(factory: LatestRemoteStorageFactoryImpl): LatestRemoteStorageFactory
//
//    @Binds
//    fun provideRepository(repository: LatestRepositoryImpl): LatestRepository
//
//    @Binds
//    fun provideGetLatestListList(useCase: GetLatestListImpl): GetLatestList
//}