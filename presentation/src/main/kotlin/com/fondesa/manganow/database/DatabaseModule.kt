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

package com.fondesa.manganow.database

import com.fondesa.data.database.DatabaseClient
import com.fondesa.data.database.SQLiteClient
import com.fondesa.data.database.injection.DatabaseInfo
import com.fondesa.data.database.strategy.DefaultErrorStrategy
import com.fondesa.data.database.strategy.DefaultUpgradeStrategy
import com.fondesa.data.database.strategy.ErrorStrategy
import com.fondesa.data.database.strategy.UpgradeStrategy
import com.fondesa.data.database.structure.AppGraph
import com.fondesa.data.database.structure.Graph
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabaseClient(client: SQLiteClient): DatabaseClient = client

    @Provides
    fun provideUpgradeStrategy(strategy: DefaultUpgradeStrategy): UpgradeStrategy = strategy

    @Provides
    fun provideErrorStrategy(strategy: DefaultErrorStrategy): ErrorStrategy = strategy

    @Provides
    fun provideGraph(graph: AppGraph): Graph = graph

    @DatabaseInfo
    @Provides
    fun provideName(): String = "app_db"

    @DatabaseInfo
    @Provides
    fun provideVersion(): Int = 1
}