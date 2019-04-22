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

package com.fondesa.manganow.time.impl

import android.os.Handler
import com.fondesa.manganow.time.api.Scheduler
import javax.inject.Inject

/**
 * Implementation of [Scheduler] which uses an Android [Handler] to schedule a task.
 */
class HandlerScheduler @Inject constructor() : Scheduler {

    private var handler: Handler? = null

    override fun schedule(time: Long, block: () -> Unit) {
        if (handler == null) {
            // Initialize the handler if not initialized before.
            handler = Handler()
        }
        // Post delay the lambda.
        handler?.postDelayed(block, time)
    }

    override fun cancel() {
        // Cancel the delayed runnable.
        handler?.removeCallbacksAndMessages(null)
    }

    override fun release() {
        cancel()
    }
}