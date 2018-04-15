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

package com.fondesa.manganow.manga.list

import com.fondesa.data.manga.repository.DefaultMangaRepository
import com.fondesa.data.manga.store.MangaCacheDataStore
import com.fondesa.data.manga.store.MangaRemoteDataStore
import com.fondesa.data.store.CacheDataStore
import com.fondesa.data.store.RemoteDataStore
import com.fondesa.domain.manga.model.Manga
import com.fondesa.domain.manga.repository.MangaRepository
import com.fondesa.domain.manga.usecase.GetMangaList
import com.fondesa.domain.usecase.UseCase
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module(includes = [MangaListModule.UseCases::class])
interface MangaListModule {

    @Binds
    fun providePresenter(presenter: MangaListPresenter): MangaListContract.Presenter

    @Binds
    fun provideView(view: MangaListActivity): MangaListContract.View

    @Binds
    fun provideMangaRepository(repository: DefaultMangaRepository): MangaRepository

    @Module
    class UseCases {

        //TODO: probably remove this checking: https://github.com/google/dagger/issues/1143

        @Provides
        fun provideGetMangaList(useCase: GetMangaList): UseCase<List<Manga>, Unit> = useCase

        @Provides
        fun provideMangaCacheDataStore(store: MangaCacheDataStore): CacheDataStore<List<Manga>> = store

        @Provides
        fun provideMangaRemoteDataStore(store: MangaRemoteDataStore): RemoteDataStore<List<Manga>> = store
    }
}