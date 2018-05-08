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

package com.fondesa.manganow.screen

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fondesa.screen.ScreenFragment
import timber.log.Timber

abstract class LoggableScreenFragment : ScreenFragment() {

    protected abstract val enableLifecycleLogging: Boolean

    override fun onAttach(context: Context) {
        super.onAttach(context)
        maybeLogLifecycle("onAttach()")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        maybeLogLifecycle("onCreate($savedInstanceState)")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        maybeLogLifecycle("onActivityCreated($savedInstanceState)")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        maybeLogLifecycle("onCreateView($savedInstanceState)")
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        maybeLogLifecycle("onViewCreated($savedInstanceState)")
    }

    override fun onStart() {
        super.onStart()
        maybeLogLifecycle("onStart()")
    }

    override fun onResume() {
        super.onResume()
        maybeLogLifecycle("onResume()")
    }

    override fun onPause() {
        super.onPause()
        maybeLogLifecycle("onPause()")
    }

    override fun onStop() {
        super.onStop()
        maybeLogLifecycle("onStop()")
    }

    override fun onDestroy() {
        super.onDestroy()
        maybeLogLifecycle("onDestroy()")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        maybeLogLifecycle("onDestroyView()")
    }

    override fun onDetach() {
        super.onDetach()
        maybeLogLifecycle("onDetach()")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        maybeLogLifecycle("onSaveInstanceState($outState)")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        maybeLogLifecycle("onActivityResult($requestCode, $resultCode, $data)")
    }

    private fun maybeLogLifecycle(name: String) {
        if (enableLifecycleLogging) {
            Timber.d("Lifecycle event for screen with tag ${key.tag}: $name")
        }
    }
}