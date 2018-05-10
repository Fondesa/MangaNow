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

package com.fondesa.manganow.adapter

import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * Interface used to specify an element that can define its [RecyclerViewInteraction]s.
 */
abstract class InteractiveViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    /**
     * @return interactions added to the [View]s of the row.
     */
    open val interactions: Array<RecyclerViewInteraction> = emptyArray()
}