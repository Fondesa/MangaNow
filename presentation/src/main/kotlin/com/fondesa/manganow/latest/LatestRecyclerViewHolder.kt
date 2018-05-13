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

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.fondesa.data.converter.Converter
import com.fondesa.domain.latest.model.Latest
import com.fondesa.manganow.R
import com.fondesa.manganow.adapter.InteractiveRecyclerViewHolder
import com.fondesa.manganow.adapter.RecyclerViewInteraction
import com.fondesa.manganow.adapter.RecyclerViewRowGesture
import com.fondesa.manganow.converter.ChapterNumberConverter
import com.fondesa.manganow.converter.PreviewDateConverter
import com.fondesa.manganow.view.inflateChild
import java.util.*

/**
 * Type of [RecyclerView.ViewHolder] used to show the view of the cell for the latest section.
 * This holder enables the click gesture on the root view.
 */
class LatestRecyclerViewHolder(parent: ViewGroup) :
    InteractiveRecyclerViewHolder(parent.inflateChild(R.layout.row_latest)) {

    override val interactions =
        arrayOf(RecyclerViewInteraction(itemView, RecyclerViewRowGesture.CLICK))

    private val context get() = itemView.context

    //TODO: inject them
    private val chapterNumberConverter: Converter<Double, String> by lazy { ChapterNumberConverter() }
    private val dateConverter: Converter<Date, String> by lazy { PreviewDateConverter(context) }

    fun bind(item: Latest) {
        val manga = item.manga
        val chapter = item.chapter
        val chapterDate = chapter.releaseDate
        val chapterTextNumber = chapterNumberConverter.convert(chapter.number)

        itemView.titleTextView.text = manga.title
        itemView.subtitleTextView.text = String.format(
            context.getString(R.string.label_chapter_number),
            chapterTextNumber
        )
        itemView.dateTextView.text = dateConverter.convert(chapterDate)

//        // Retrieve the image task externally.
//        val imageTask = listener.getImageTask(item)
//        // Load the image.
//        imageLoader.loadImage(holder.imageView, imageTask)
    }
}