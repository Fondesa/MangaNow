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

package com.fondesa.manganow.ui.api.view

import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.fondesa.manganow.log.api.Log
import com.fondesa.manganow.thread.api.launchWithDelay
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

/**
 * Created by antoniolig on 25/09/17.
 */
class AutoSubmitQueryTextListener(
    private val thresholdMs: Long = DEFAULT_THRESHOLD_MS,
    private inline val onChange: (String) -> Unit
) : SearchView.OnQueryTextListener, CoroutineScope, LifecycleObserver {

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Default

    private val job = Job()

    private var currentText: String = ""

    override fun onQueryTextSubmit(text: String): Boolean {
        if (currentText == text) {
            return true
        }

        currentText = text

        job.cancelChildren()

        Log.d("Search text submitted: $text")
        onChange(text)
        return true
    }

    override fun onQueryTextChange(text: String): Boolean {
        if (currentText == text) {
            return true
        }

        currentText = text

        job.cancelChildren()

        launchWithDelay(thresholdMs) {
            Log.d("Search text changed: $text")
            withContext(Dispatchers.Main) {
                onChange(text)
            }
        }
        return true
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        job.cancel()
    }

    companion object {
        private const val DEFAULT_THRESHOLD_MS: Long = 500
    }
}