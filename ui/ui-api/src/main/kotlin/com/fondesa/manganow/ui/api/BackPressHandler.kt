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

package com.fondesa.manganow.ui.api

import android.app.Activity

/**
 * Used to handle the back press in another component.
 */
interface BackPressHandler {

    /**
     * Manages the back press for the component that implements it.
     *
     * @return true if the back press is totally handled by this component,
     * false if after the handling done by this component, the back press must be handled
     * by the [Activity].
     */
    fun handleBackPress(): Boolean
}