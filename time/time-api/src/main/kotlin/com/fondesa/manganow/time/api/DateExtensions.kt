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

package com.fondesa.manganow.time.api

import java.util.*

val Date.isToday: Boolean
    get() {
        val dateCalendar = Calendar.getInstance()
        dateCalendar.time = this

        val comparisonCalendar = Calendar.getInstance()
        return dateCalendar isSameDay comparisonCalendar
    }

val Date.isYesterday: Boolean
    get() {
        val dateCalendar = Calendar.getInstance()
        dateCalendar.time = this

        val comparisonCalendar = Calendar.getInstance()
        // Move the calendar date to yesterday.
        comparisonCalendar.add(Calendar.DAY_OF_YEAR, -1)
        return dateCalendar isSameDay comparisonCalendar
    }