package com.fondesa.manganow.time

import android.os.Handler
import java.util.*

/**
 * Used to schedule a task that will be executed after a certain period of time.
 * The [Scheduler] is only a layer to avoid the direct usage of [Handler] or [Timer].
 */
interface Scheduler {

    /**
     * Schedules a task that will be executed after a certain period of time.
     *
     * @param time amount of time after which the task must be executed.
     * @param block task that must be executed.
     */
    fun schedule(time: Long, block: () -> Unit)

    /**
     * Cancel the scheduling of the task.
     */
    fun cancel()

    /**
     * Releases any resource related to the task and cancel the scheduling.
     */
    fun release()
}