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

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.SpinnerAdapter
import androidx.core.view.ViewCompat

abstract class SpinnerAdapter<T, MVH : SpinnerViewHolder, DVH : SpinnerViewHolder>(
    context: Context
) : ArrayAdapter<T>(context, 0), SpinnerAdapter {

    private val holderTag = ViewCompat.generateViewId()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val holder = if (convertView == null) {
            onCreateMainViewHolder(parent).apply {
                containerView.setTag(holderTag, this)
            }
        } else {
            @Suppress("UNCHECKED_CAST")
            convertView.getTag(holderTag) as MVH
        }
        onBindMainViewHolder(holder, position)
        return holder.containerView
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val holder = if (convertView == null) {
            onCreateDropDownViewHolder(parent).apply {
                containerView.setTag(holderTag, this)
            }
        } else {
            @Suppress("UNCHECKED_CAST")
            convertView.getTag(holderTag) as DVH
        }
        onBindDropDownViewHolder(holder, position)
        return holder.containerView
    }

    open fun updateList(items: List<T>) {
        clear()
        addAll(items)
        notifyDataSetChanged()
    }

    protected abstract fun onCreateMainViewHolder(parent: ViewGroup): MVH

    protected abstract fun onCreateDropDownViewHolder(parent: ViewGroup): DVH

    protected abstract fun onBindMainViewHolder(holder: MVH, position: Int)

    protected abstract fun onBindDropDownViewHolder(holder: DVH, position: Int)
}