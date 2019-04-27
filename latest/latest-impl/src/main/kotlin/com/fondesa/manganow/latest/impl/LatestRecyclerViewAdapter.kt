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

package com.fondesa.manganow.latest.impl

import android.view.View
import android.view.ViewGroup
import com.fondesa.manganow.ui.api.view.InteractiveRecyclerViewHolder
import com.fondesa.manganow.ui.api.view.PagingRecyclerViewAdapter

class LatestRecyclerViewAdapter(
    pageSize: Int,
    private val holderFactory: LatestRecyclerViewHolderFactory,
    private val clickListener: OnLatestClickListener
) : PagingRecyclerViewAdapter(pageSize) {

    private val items = mutableListOf<Latest>()

    init {
        // The ids are stable to optimize the performances.
        setHasStableIds(true)
    }

    fun updateList(items: LatestList) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    override fun getItemId(position: Int): Long {
        val latest = items.getOrNull(position) ?: return 0
        // The id of the item never changes.
        return latest.manga.id
    }

    override fun getItemSize(): Int = items.size

    override fun onConstructItemViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): InteractiveRecyclerViewHolder = holderFactory.create(parent)

    override fun onBindViewHolder(holder: InteractiveRecyclerViewHolder, position: Int) {
        holder.onItem<LatestRecyclerViewHolder> {
            bind(items[position])
        }
    }

    override fun onCellClick(view: View, position: Int) {
        super.onCellClick(view, position)
        val latest = items[position]
        clickListener.onLatestClicked(latest)
    }

    interface OnLatestClickListener {

        fun onLatestClicked(latest: Latest)
    }
}