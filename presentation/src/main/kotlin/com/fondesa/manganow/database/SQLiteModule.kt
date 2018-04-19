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

import com.fondesa.database.injection.SQLiteDatabaseInfo
import com.fondesa.database.strategy.DefaultErrorStrategy
import com.fondesa.database.strategy.DefaultUpgradeStrategy
import com.fondesa.database.strategy.ErrorStrategy
import com.fondesa.database.strategy.UpgradeStrategy
import com.fondesa.database.structure.Graph
import com.fondesa.database.structure.Table
import dagger.Module
import dagger.Provides
import javax.inject.Inject
import javax.inject.Singleton

@Module
class SQLiteModule {

    @Provides
    fun provideUpgradeStrategy(strategy: DefaultUpgradeStrategy): UpgradeStrategy = strategy

    @Provides
    fun provideErrorStrategy(strategy: DefaultErrorStrategy): ErrorStrategy = strategy

    @Singleton
    @Provides
    fun provideGraph(graph: ToBeRemovedGraph): Graph = graph

    @SQLiteDatabaseInfo
    @Provides
    fun provideName(): String = "app_db"

    @SQLiteDatabaseInfo
    @Provides
    fun provideVersion(): Int = 26

    class ToBeRemovedGraph @Inject constructor(): Graph {
        override fun getTables(): Array<Table> {
            return emptyArray()
        }
    }
}