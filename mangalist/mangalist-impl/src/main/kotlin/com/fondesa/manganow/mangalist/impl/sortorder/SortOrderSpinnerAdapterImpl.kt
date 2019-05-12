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

package com.fondesa.manganow.mangalist.impl.sortorder

import android.content.Context
import android.view.ViewGroup
import javax.inject.Inject
import javax.inject.Named

class SortOrderSpinnerAdapterImpl @Inject constructor(
    context: Context,
    @Named(SortOrderSpinnerViewHolderFactory.MAIN) private val mainHolderFactory: SortOrderSpinnerViewHolderFactory,
    @Named(SortOrderSpinnerViewHolderFactory.DROPDOWN) private val dropDownHolderFactory: SortOrderSpinnerViewHolderFactory
) : SortOrderSpinnerAdapter(context) {

    override fun onCreateMainViewHolder(parent: ViewGroup): SortOrderSpinnerViewHolder =
        mainHolderFactory.create(parent)

    override fun onCreateDropDownViewHolder(parent: ViewGroup): SortOrderSpinnerViewHolder =
        dropDownHolderFactory.create(parent)

    override fun onBindMainViewHolder(holder: SortOrderSpinnerViewHolder, position: Int) {
        val item = getItem(position)
            ?: throw IllegalStateException("The item at position $position shouldn't be null.")
        holder.bind(item)
    }

    override fun onBindDropDownViewHolder(holder: SortOrderSpinnerViewHolder, position: Int) {
        val item = getItem(position)
            ?: throw IllegalStateException("The item at position $position shouldn't be null.")
        holder.bind(item)
    }
}