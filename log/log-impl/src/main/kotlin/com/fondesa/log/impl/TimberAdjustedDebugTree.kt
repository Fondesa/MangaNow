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

package com.fondesa.log.impl

import com.fondesa.log.api.Logger
import timber.log.Timber

/**
 * Used to adjust the stack index of [Timber.DebugTree] to skip the logging facade's indexes.
 */
class TimberAdjustedDebugTree : Timber.DebugTree() {

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        val adjustedTag = createAdjustedTag()
        super.log(priority, adjustedTag, message, t)
    }

    private fun createAdjustedTag(): String? {
        val stackTrace = Throwable().stackTrace
        if (stackTrace.size <= ADJUSTED_CALL_STACK_INDEX) {
            throw IllegalStateException(
                "Synthetic stacktrace didn't have enough elements: are you using proguard?"
            )
        }
        return createStackElementTag(stackTrace[ADJUSTED_CALL_STACK_INDEX])
    }

    companion object {

        /**
         * The [Timber.DebugTree]'s index is 5 by default but the class [TimberLogger] and
         * the interface [Logger] should be skipped.
         */
        private const val ADJUSTED_CALL_STACK_INDEX = 7
    }
}