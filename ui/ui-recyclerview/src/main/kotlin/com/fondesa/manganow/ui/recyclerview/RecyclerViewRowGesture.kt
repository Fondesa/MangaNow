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

package com.fondesa.manganow.ui.recyclerview

import androidx.recyclerview.widget.RecyclerView

/**
 * Types of gestures that user could perform on a [RecyclerView]'s row.
 */
enum class RecyclerViewRowGesture {

    /**
     * Used to identify a click gesture.
     */
    CLICK,

    /**
     * Used to identify a long-click gesture.
     */
    LONG_CLICK,

    /**
     * Used to identify a touch gesture.
     */
    TOUCH
}