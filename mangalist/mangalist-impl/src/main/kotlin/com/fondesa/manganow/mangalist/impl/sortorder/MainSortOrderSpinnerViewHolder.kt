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

import android.graphics.Color
import android.view.ViewGroup
import android.widget.TextView
import com.fondesa.manganow.mangalist.api.sortorder.SortOrder
import com.fondesa.manganow.ui.api.R
import com.fondesa.manganow.ui.api.util.inflateChild
import com.google.auto.factory.AutoFactory

@AutoFactory(implementing = [SortOrderSpinnerViewHolderFactory::class])
class MainSortOrderSpinnerViewHolder(parent: ViewGroup) :
    SortOrderSpinnerViewHolder(parent.inflateChild(R.layout.support_simple_spinner_dropdown_item)) {

    private val textView = containerView.findViewById<TextView>(android.R.id.text1).apply {
        setTextColor(Color.WHITE)
    }

    override fun bind(item: SortOrder) {
        textView.text = item.name
    }
}