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

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

class RecyclerViewScrollEndedListener(
    private val threshold: Int = 0,
    private inline val onEnd: () -> Unit
) : RecyclerView.OnScrollListener() {

    private var currentTotalItemCount = 0

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        val layoutManager = recyclerView.layoutManager ?: return

        val totalItemCount = layoutManager.itemCount
        if (totalItemCount <= 0)
            return

        if (currentTotalItemCount != totalItemCount) {
            // Get the visible items in the RecyclerView.
            val visibleItemCount = recyclerView.childCount
            // Get the position of the first visible item.
            val firstVisiblePosition = layoutManager.firstVisiblePosition()
            if ((totalItemCount - visibleItemCount) <= (firstVisiblePosition + threshold)) {
                // Reached the end of the list, load the next page.
                onEnd()
                // Cache the total item count to avoid unnecessary loadings.
                currentTotalItemCount = totalItemCount
            }
        }
    }

    /**
     * Reset the configurations of the scroll when the content changes.
     */
    fun reset() {
        // Bring the item count to the default value.
        currentTotalItemCount = 0
    }

    private fun RecyclerView.LayoutManager.firstVisiblePosition() =
        when (this) {
            is LinearLayoutManager -> findFirstVisibleItemPosition()
            is StaggeredGridLayoutManager -> findFirstVisibleItemPositions(null)[0]
            else -> throw ClassCastException(
                "The layout manager needs to subclass ${LinearLayoutManager::class.java.name} or " +
                        StaggeredGridLayoutManager::class.java.name
            )
        }
}