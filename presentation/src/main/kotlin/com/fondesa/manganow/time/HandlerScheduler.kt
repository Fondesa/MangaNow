package com.fondesa.manganow.time

import android.os.Handler
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