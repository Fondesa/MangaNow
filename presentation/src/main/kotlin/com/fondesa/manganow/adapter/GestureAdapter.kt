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
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup

/**
 * Adapter used to define the gestures on the rows of a [RecyclerView].
 *
 * @param VH type of [RecyclerView.ViewHolder].
 */
abstract class GestureAdapter<VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {

    /**
     * Constructs the [VH] for a given [View].
     *
     * @param parent root [View] of the cell.
     * @param viewType the view type of the new [View].
     * @return new instance of [VH].
     */
    abstract fun onConstructViewHolder(parent: ViewGroup, viewType: Int): VH

    /**
     * Called when a view in a row (or its root view) is clicked.
     *
     * @param view current [View] of the cell.
     * @param position current position.
     */
    open fun onCellClick(view: View, position: Int) = Unit

    /**
     * Called when a view in a row (or its root view) is long-clicked.
     *
     * @param view current [View] of the cell.
     * @param position current position.
     */
    open fun onCellLongClick(view: View, position: Int): Boolean = false

    /**
     * Called when a view in a row (or its root view) is touched.
     *
     * @param view current [View] of the cell.
     * @param position current position.
     * @param event event triggered.
     */
    open fun onCellTouch(view: View, position: Int, event: MotionEvent): Boolean = false

    final override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val holder = onConstructViewHolder(parent, viewType)
        // The interactions are added only if the ViewHolder is Interactive.
        (holder as? Interactive)?.interactions?.forEach {
            addGestureToView(holder, it.view, it.gesture)
        }
        return holder
    }

    private fun addGestureToView(holder: VH, v: View, gesture: Gesture) {
        when (gesture) {
            Gesture.CLICK -> v.setOnClickListener { view ->
                onCellClick(view, holder.adapterPosition)
            }
            Gesture.LONG_CLICK -> v.setOnLongClickListener { view ->
                onCellLongClick(view, holder.adapterPosition)
            }
            Gesture.TOUCH -> v.setOnTouchListener { view, event ->
                onCellTouch(view, holder.adapterPosition, event)
            }
        }
    }
}