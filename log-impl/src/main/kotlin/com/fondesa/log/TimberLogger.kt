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

package com.fondesa.log

import com.fondesa.common.log.Logger
import timber.log.Timber
import javax.inject.Inject

/**
 * Implementation of [Logger] which uses [Timber].
 */
class TimberLogger @Inject constructor() : Logger {

    init {
        if (BuildConfig.DEBUG) {
            // Log only in debug.
            Timber.plant(TimberAdjustedDebugTree())
        }
    }

    override fun d(message: String) {
        Timber.d(message)
    }

    override fun i(message: String) {
        Timber.i(message)
    }

    override fun w(message: String) {
        Timber.w(message)
    }

    override fun e(message: String) {
        Timber.e(message)
    }

    override fun e(t: Throwable) {
        Timber.e(t)
    }
}