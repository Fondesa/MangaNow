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

import android.view.ViewGroup

/**
 * Type of [InteractiveRecyclerViewAdapter] used to handle the pagination.
 * When the page size is reached, this adapter will show the [PagingRecyclerViewHolder] if the pagination
 * is enabled.
 */
abstract class PagingRecyclerViewAdapter(private val pageSize: Int) : InteractiveRecyclerViewAdapter() {

    private val showProgress: Boolean
        get() {
            val pageSize = pageSize
            val count = getItemSize()
            return count != 0 && count % pageSize == 0
        }

    final override fun getItemViewType(position: Int): Int {
        val itemViewType = getViewType(position)
        if (itemViewType == VIEW_TYPE_PROGRESS)
            throw IllegalArgumentException("You can't use the value $VIEW_TYPE_PROGRESS for an item view type.")

        if (!showProgress)
            return itemViewType

        return if (position == itemCount - 1)
            VIEW_TYPE_PROGRESS
        else
            itemViewType
    }

    final override fun getItemCount(): Int {
        val itemCount = getItemSize()
        // The item count is increased if the progress must be shown.
        return if (showProgress) itemCount + 1 else itemCount
    }

    final override fun onConstructViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): InteractiveRecyclerViewHolder {
        return if (viewType == VIEW_TYPE_PROGRESS) {
            onConstructProgressViewHolder(parent, viewType)
        } else {
            onConstructItemViewHolder(parent, viewType)
        }
    }

    protected open fun getViewType(position: Int): Int = 0

    protected abstract fun getItemSize(): Int

    protected abstract fun onConstructItemViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): InteractiveRecyclerViewHolder

    protected open fun onConstructProgressViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): InteractiveRecyclerViewHolder = PagingRecyclerViewHolder(parent)

    protected val InteractiveRecyclerViewHolder.isProgressViewHolder get() = itemViewType == VIEW_TYPE_PROGRESS

    protected val InteractiveRecyclerViewHolder.isItemViewHolder get() = !isProgressViewHolder

    protected inline fun <reified T : InteractiveRecyclerViewHolder> InteractiveRecyclerViewHolder.onItem(
        block: T.() -> Unit
    ) {
        if (isItemViewHolder && this is T) {
            block(this)
        }
    }

    protected inline fun <reified T : InteractiveRecyclerViewHolder> InteractiveRecyclerViewHolder.onProgress(
        block: T.() -> Unit
    ) {
        if (isProgressViewHolder && this is T) {
            block(this)
        }
    }

    companion object {
        private const val VIEW_TYPE_PROGRESS = 1003
    }
}