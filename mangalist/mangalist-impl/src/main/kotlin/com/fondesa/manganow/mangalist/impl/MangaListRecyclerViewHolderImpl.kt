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

package com.fondesa.manganow.mangalist.impl

import android.view.ViewGroup
import com.fondesa.manganow.domain.manga.Manga
import com.fondesa.manganow.remote.api.task.RemoteImageUrl
import com.fondesa.manganow.ui.api.util.inflateChild
import com.fondesa.manganow.ui.api.view.RecyclerViewInteraction
import com.fondesa.manganow.ui.api.view.RecyclerViewRowGesture
import com.google.auto.factory.AutoFactory
import kotlinx.android.synthetic.main.row_manga.*

@AutoFactory(implementing = [MangaListRecyclerViewHolderFactory::class])
class MangaListRecyclerViewHolderImpl(parent: ViewGroup) :
    MangaListRecyclerViewHolder(parent.inflateChild(R.layout.row_manga)) {

    override val interactions: Array<RecyclerViewInteraction> =
        arrayOf(RecyclerViewInteraction(itemView, RecyclerViewRowGesture.CLICK))

    override fun bind(item: Manga) {
        titleTextView.text = item.title
        val imageUrl = item.imageUrl?.let {
            RemoteImageUrl.from(it)
        }
        imageView.setImageURI(imageUrl)
    }
}

