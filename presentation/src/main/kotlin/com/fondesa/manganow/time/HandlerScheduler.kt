package com.fondesa.manganow.time

import android.os.Handler

/**
 * Implementation of [Scheduler] that uses an Android [Handler] to schedule a task.
 */
class HandlerScheduler : Scheduler {

    private var handler: Handler? = null

    override fun schedule(time: Long, block: () -> Unit) {
        if (handler == null) {
            // Initialize the handler if not initialized before.
            handler = Handler()
        }
        // post delay the block.
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