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

import android.view.ViewGroup
import com.fondesa.manganow.remote.api.task.RemoteImageUrl
import com.fondesa.manganow.time.api.isToday
import com.fondesa.manganow.time.api.isYesterday
import com.fondesa.manganow.ui.api.util.inflateChild
import com.fondesa.manganow.ui.api.view.RecyclerViewInteraction
import com.fondesa.manganow.ui.api.view.RecyclerViewRowGesture
import com.fondesa.manganow.ui.api.view.context
import com.google.auto.factory.AutoFactory
import kotlinx.android.synthetic.main.row_latest.*
import java.text.DateFormat
import java.text.DecimalFormat
import java.util.*

@AutoFactory(implementing = [LatestRecyclerViewHolderFactory::class])
class LatestRecyclerViewHolderImpl(parent: ViewGroup) :
    LatestRecyclerViewHolder(parent.inflateChild(R.layout.row_latest)) {

    private val timeFormat by lazy { DateFormat.getTimeInstance(DateFormat.SHORT) }
    private val dateFormat by lazy { DateFormat.getDateInstance(DateFormat.SHORT) }
    private val decimalFormat by lazy { DecimalFormat("#.#") }

    override val interactions: Array<RecyclerViewInteraction> =
        arrayOf(RecyclerViewInteraction(itemView, RecyclerViewRowGesture.CLICK))

    override fun bind(item: Latest) {
        val manga = item.manga
        val chapter = item.chapter
        val chapterDate = chapter.releaseDate
        val chapterTextNumber = decimalFormat.format(chapter.number)

        titleTextView.text = manga.title
        subtitleTextView.text =
            String.format(context.getString(R.string.label_chapter_number), chapterTextNumber)
        dateTextView.text = chapterDate.toReadableDate()
        val imageUrl = manga.imageUrl?.let {
            RemoteImageUrl.from(it)
        }
        imageView.setImageURI(imageUrl)
    }

    private fun Date.toReadableDate(): String = when {
        isToday -> timeFormat.format(this)
        isYesterday -> context.getString(R.string.label_yesterday)
        else -> dateFormat.format(this)
    }
}

