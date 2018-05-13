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

package com.fondesa.manganow.latest

import android.view.ViewGroup
import com.fondesa.domain.latest.LatestList
import com.fondesa.domain.latest.model.Latest
import com.fondesa.manganow.adapter.InteractiveRecyclerViewHolder
import com.fondesa.manganow.adapter.PagingRecyclerViewAdapter

class LatestRecyclerViewAdapter : PagingRecyclerViewAdapter(LatestContract.PAGE_SIZE) {

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
    ): InteractiveRecyclerViewHolder = LatestRecyclerViewHolder(parent)

    override fun onBindViewHolder(holder: InteractiveRecyclerViewHolder, position: Int) {
        holder.onItem<LatestRecyclerViewHolder> {
            bind(items[position])
        }
    }
}