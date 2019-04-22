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

package com.fondesa.manganow.database.di

import com.fondesa.manganow.core.api.AppInitializer
import com.fondesa.manganow.database.api.client.DatabaseClient
import com.fondesa.manganow.database.api.structure.Graph
import com.fondesa.manganow.database.impl.DatabaseInitializer
import com.fondesa.manganow.database.impl.GraphImpl
import com.fondesa.manganow.database.impl.SQLiteClient
import com.fondesa.manganow.database.impl.qualifiers.SQLiteDatabaseInfo
import com.fondesa.manganow.database.impl.strategy.DropAllUpgradeStrategy
import com.fondesa.manganow.database.impl.strategy.ErrorStrategy
import com.fondesa.manganow.database.impl.strategy.UpgradeStrategy
import com.fondesa.manganow.database.impl.strategy.VacuumErrorStrategy
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet
import javax.inject.Singleton

@Module(includes = [DatabaseModule.WithProvides::class])
interface DatabaseModule {

    @Singleton
    @Binds
    fun provideDatabaseClient(client: SQLiteClient): DatabaseClient

    @Binds
    @IntoSet
    fun provideDatabaseInitializer(initializer: DatabaseInitializer): AppInitializer

    @Binds
    fun provideGraph(graph: GraphImpl): Graph

    @Module
    object WithProvides {

        @JvmStatic
        @Provides
        fun provideUpgradeStrategy(strategy: DropAllUpgradeStrategy): UpgradeStrategy = strategy

        @JvmStatic
        @Provides
        fun provideErrorStrategy(strategy: VacuumErrorStrategy): ErrorStrategy = strategy

        @JvmStatic
        @SQLiteDatabaseInfo
        @Provides
        fun provideName(): String = "app_db"

        @JvmStatic
        @SQLiteDatabaseInfo
        @Provides
        fun provideVersion(): Int = 1
    }
}