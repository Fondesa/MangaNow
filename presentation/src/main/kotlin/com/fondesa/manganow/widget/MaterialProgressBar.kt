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

package com.fondesa.manganow.widget

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.annotation.ColorInt
import android.support.v4.graphics.drawable.DrawableCompat
import android.util.AttributeSet
import android.widget.ProgressBar
import com.fondesa.manganow.R
import com.fondesa.manganow.util.ColorUtil

/**
 * Custom [ProgressBar] according to material design that can be tinted across all system apis.
 * The progress bar color can be set also via XML with the attribute <i>progressColor</i>.
 */
class MaterialProgressBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.progressBarStyle
) : ProgressBar(context, attrs, defStyleAttr) {

    init {
        // The default color will be the color accent defined in the theme.
        @ColorInt val colorAccent: Int = ColorUtil.getAccent(context)

        @ColorInt val progressColor: Int
        if (attrs != null) {
            val typedArr = context.obtainStyledAttributes(attrs, R.styleable.MaterialProgressBar)
            // Read the attributes.
            progressColor =
                    typedArr.getColor(R.styleable.MaterialProgressBar_progressColor, colorAccent)
            // Recycle the array.
            typedArr.recycle()
        } else {
            progressColor = colorAccent
        }

        setProgressColor(progressColor)
    }

    /**
     * Set the color of the progress bar.
     *
     * @param progressColor resolved color that must be set to the progress bar.
     */
    fun setProgressColor(@ColorInt progressColor: Int) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            val wrappedDrawable = DrawableCompat.wrap(indeterminateDrawable)
            // Tint the wrapper drawable.
            DrawableCompat.setTint(wrappedDrawable, progressColor)
            // Change the indeterminate drawable.
            indeterminateDrawable = DrawableCompat.unwrap<Drawable>(wrappedDrawable)
        } else {
            // Create the tint list from the color.
            indeterminateTintList = ColorStateList.valueOf(progressColor)
        }
    }
}