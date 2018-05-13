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

package com.fondesa.manganow.converter

import android.content.Context
import com.fondesa.data.converter.Converter
import com.fondesa.manganow.R
import java.text.DateFormat
import java.util.*

/**
 * Implementation of [Converter] used to convert a [Date] to a readable text representing it.
 * This converter will show the time if the date is today, the label [R.string.label_yesterday] if
 * the date is yesterday and the formatted date if the date is before yesterday.
 *
 * @param context [Context] used to access to resources.
 */
class PreviewDateConverter(context: Context) : Converter<Date, String> {

    private val yesterdayLabel = context.getString(R.string.label_yesterday)
    private val timeFormat by lazy { DateFormat.getTimeInstance(DateFormat.SHORT) }
    private val dateFormat by lazy { DateFormat.getDateInstance(DateFormat.SHORT) }

    override fun convert(value: Date): String {

        val dateCalendar = Calendar.getInstance()
        dateCalendar.time = value

        // The calendar date is today.
        val comparisonCalendar = Calendar.getInstance()

        if (isSameDay(dateCalendar, comparisonCalendar)) {
            // The date is today.
            return timeFormat.format(value)
        }

        // Move the calendar date to yesterday.
        comparisonCalendar.add(Calendar.DAY_OF_YEAR, -1)
        if (isSameDay(dateCalendar, comparisonCalendar)) {
            // The date is yesterday.
            return yesterdayLabel
        }

        // The date is before yesterday.
        return dateFormat.format(value)
    }

    private fun isSameDay(first: Calendar, second: Calendar) = first.get(Calendar.ERA) == second.get(Calendar.ERA) &&
            first.get(Calendar.YEAR) == second.get(Calendar.YEAR) &&
            first.get(Calendar.DAY_OF_YEAR) == second.get(Calendar.DAY_OF_YEAR)
}