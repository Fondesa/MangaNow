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

package com.fondesa.manganow.util

import android.content.Context
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import com.fondesa.manganow.R

/**
 * Utility class used to manage colors.
 */
object ColorUtil {

    /**
     * Get the resolved color related to the [R.attr.colorPrimary] attribute of the theme.
     *
     * @param context [Context] used to access to the theme.
     * @return resolved primary color.
     */
    @ColorInt
    fun getPrimary(context: Context): Int = getColorAttr(context, R.attr.colorPrimary)

    /**
     * Get the resolved color related to the [R.attr.colorPrimaryDark] attribute of the theme.
     *
     * @param context [Context] used to access to the theme.
     * @return resolved primary dark color.
     */
    @ColorInt
    fun getPrimaryDark(context: Context): Int = getColorAttr(context, R.attr.colorPrimaryDark)

    /**
     * Get the resolved color related to the [R.attr.colorAccent] attribute of the theme.
     *
     * @param context [Context] used to access to the theme.
     * @return resolved accent color.
     */
    @ColorInt
    fun getAccent(context: Context): Int = getColorAttr(context, R.attr.colorAccent)

    @ColorInt
    fun getTextPrimary(context: Context): Int = getColorAttr(context, android.R.attr.textColorPrimary)

    @ColorInt
    fun getTextSecondary(context: Context): Int = getColorAttr(context, android.R.attr.textColorSecondary)

    @ColorInt
    private fun getColorAttr(context: Context, @AttrRes colorAttr: Int): Int {
        val typedValue = TypedValue()
        val theme = context.theme
        theme.resolveAttribute(colorAttr, typedValue, true)
        return typedValue.data
    }
}