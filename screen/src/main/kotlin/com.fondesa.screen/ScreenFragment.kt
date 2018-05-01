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

package com.fondesa.screen

import android.content.Context
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.annotation.StyleRes
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.android.support.DaggerFragment

abstract class ScreenFragment : DaggerFragment() {

    protected val screenManager
        get() = _screenManager ?: throw ScreenManagerNotAttachedException()

    private var _screenManager: ScreenManager? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        _screenManager = context as? ScreenManager ?: throw ClassCastException(
            "Cannot cast ${context::class.java.name} to ${ScreenManager::class.java.name}"
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val correctInflater = theme()?.let {
            // Wrap the custom theme.
            val contextThemeWrapper = ContextThemeWrapper(activity, it)
            // Clone the inflater passing the custom theme.
            inflater.cloneInContext(contextThemeWrapper)
        } ?: inflater
        return correctInflater.inflate(rootLayout(), container, false)
    }

    override fun onDetach() {
        super.onDetach()
        _screenManager = null
    }

    @LayoutRes
    protected abstract fun rootLayout(): Int

    @StyleRes
    protected open fun theme(): Int? = null
}