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

package com.fondesa.manganow.log.impl

import com.fondesa.manganow.log.api.Logger
import dagger.Reusable
import timber.log.Timber
import javax.inject.Inject

/**
 * Implementation of [Logger] which uses [Timber].
 */
@Reusable
class TimberLogger @Inject constructor() : Logger {

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