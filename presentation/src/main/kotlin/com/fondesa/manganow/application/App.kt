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

package com.fondesa.manganow.application

import com.fondesa.common.database.DatabaseClient
import com.fondesa.manganow.BuildConfig
import com.fondesa.manganow.injection.application.DaggerAppComponent
import com.squareup.leakcanary.LeakCanary
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import timber.log.Timber
import timber.log.Timber.DebugTree
import javax.inject.Inject

class App : DaggerApplication() {

    @Inject
    lateinit var databaseClient: DatabaseClient

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            // Log only in debug.
            Timber.plant(DebugTree())
        }

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // In this case the app mustn't be initialized.
            return
        }
        // Install LeakCanary to enable the leak monitoring.
        LeakCanary.install(this)

        databaseClient.createDatabase()
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        val appComponent = DaggerAppComponent.builder()
            .application(this)
            .build()

        appComponent.inject(this)
        return appComponent
    }
}