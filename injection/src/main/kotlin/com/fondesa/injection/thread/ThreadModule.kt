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

package com.fondesa.injection.thread

import com.fondesa.common.thread.execution.ExecutorFactory
import com.fondesa.common.thread.execution.ExecutorPool
import com.fondesa.thread.execution.CoroutinesExecutorFactory
import com.fondesa.thread.execution.ListExecutorPool
import dagger.Binds
import dagger.Module

@Module(includes = [CoroutinesModule::class])
interface ThreadModule {

    @Binds
    fun provideExecutorBuilderFactory(factory: CoroutinesExecutorFactory): ExecutorFactory

    @Binds
    fun provideExecutorPool(pool: ListExecutorPool): ExecutorPool
}