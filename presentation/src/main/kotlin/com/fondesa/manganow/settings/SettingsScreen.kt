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

package com.fondesa.manganow.settings

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fondesa.manganow.R
import com.fondesa.manganow.lyra
import com.fondesa.manganow.screen.BaseScreen

class SettingsScreen : BaseScreen() {

    override val rootLayout = R.layout.screen_settings

    override val title get() = getString(R.string.section_settings)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        lyra("onActivityCreated")
        super.onActivityCreated(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        lyra("onCreateView")
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lyra("onViewCreated")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lyra("onCreate")
    }

    override fun onPause() {
        super.onPause()
        lyra("onPause")
    }

    override fun onResume() {
        super.onResume()
        lyra("onResume")
    }

    override fun onStart() {
        super.onStart()
        lyra("onStart")
    }

    override fun onStop() {
        super.onStop()
        lyra("onStop")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        lyra("onAttach")
    }

    override fun onDetach() {
        super.onDetach()
        lyra("onDetach")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        lyra("onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        lyra("onDestroy")
    }
}