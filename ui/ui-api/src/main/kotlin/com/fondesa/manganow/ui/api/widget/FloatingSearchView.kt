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

package com.fondesa.manganow.ui.api.widget

import android.app.Activity
import android.app.SearchManager
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.GradientDrawable
import android.speech.RecognizerIntent
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import com.fondesa.manganow.log.api.Log
import com.fondesa.manganow.ui.api.R
import java.util.*

class FloatingSearchView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : SearchView(context, attrs, defStyleAttr) {

    private val mainTextView by lazy { findViewById<SearchAutoComplete>(R.id.search_src_text) }

    @ColorInt
    var iconsColor: Int = 0
        set(value) {
            field = value
            setColorFilterOnChildren(this, value)
        }

    @ColorInt
    var textColor: Int = 0
        set(value) {
            field = value
            mainTextView.setTextColor(value)
        }

    @ColorInt
    var hintTextColor: Int = 0
        set(value) {
            field = value
            mainTextView.setHintTextColor(value)
        }

    @ColorInt
    var shapeBackgroundColor: Int = 0
        set(value) {
            field = value
            // Create the shape.
            val shapeBg = GradientDrawable()
            shapeBg.shape = GradientDrawable.RECTANGLE
            shapeBg.cornerRadius = cornerRadius
            shapeBg.setColor(value)
            ViewCompat.setBackground(this, shapeBg)
        }

    var cornerRadius: Float = 0f
        set(value) {
            field = value
            // Create the shape.
            val shapeBg = GradientDrawable()
            shapeBg.shape = GradientDrawable.RECTANGLE
            shapeBg.cornerRadius = value
            shapeBg.setColor(shapeBackgroundColor)
            ViewCompat.setBackground(this, shapeBg)
        }

    init {
        setIconifiedByDefault(false)

        val collapsedImageView = findViewById<ImageView>(R.id.search_mag_icon)
        collapsedImageView.setImageDrawable(null)

        val closeBtn = findViewById<ImageView>(R.id.search_close_btn)
        closeBtn.setImageResource(R.drawable.ic_close)

        val voiceBtn = findViewById<ImageView>(R.id.search_voice_btn)
        voiceBtn.setImageResource(R.drawable.ic_microphone)
        voiceBtn.setPadding(
            closeBtn.paddingLeft,
            closeBtn.paddingTop,
            closeBtn.paddingRight,
            closeBtn.paddingBottom
        )
        voiceBtn.setOnClickListener {
            openVoiceDialog()
        }

        // Copy the configurations of the voice button to the up button and add it to the layout.
        val searchIcon = ImageView(context)
        // Creates a new drawable using the voice button configurations
        ViewCompat.setBackground(this, voiceBtn.background.constantState?.newDrawable())
        searchIcon.setImageResource(R.drawable.ic_search)
        val searchIconLp = LayoutParams(voiceBtn.layoutParams.width, voiceBtn.layoutParams.height)

        val padding = resources.getDimensionPixelSize(R.dimen.search_icon_padding)
        searchIcon.setPadding(padding, 0, padding, 0)
        // Add the search icon to the layout at position 0
        addView(searchIcon, 0, searchIconLp)

        val currentTextColor = mainTextView.currentTextColor
        val currentHintTextColor = mainTextView.currentHintTextColor

        if (attrs != null) {
            val typedArr = context.obtainStyledAttributes(attrs, R.styleable.FloatingSearchView)
            // Read the attributes.
            queryHint = typedArr.getString(R.styleable.FloatingSearchView_searchHint)
            cornerRadius = typedArr.getDimension(R.styleable.FloatingSearchView_cornerRadius, 0f)
            shapeBackgroundColor =
                typedArr.getColor(R.styleable.FloatingSearchView_backgroundColor, Color.WHITE)
            iconsColor = typedArr.getColor(R.styleable.FloatingSearchView_iconsColor, Color.DKGRAY)
            textColor =
                typedArr.getColor(R.styleable.FloatingSearchView_textColor, currentTextColor)
            hintTextColor = typedArr.getColor(
                R.styleable.FloatingSearchView_hintTextColor,
                currentHintTextColor
            )

            val elevation = typedArr.getDimension(R.styleable.FloatingSearchView_elevation, 0f)
            ViewCompat.setElevation(this, elevation)
            // Recycle the array.
            typedArr.recycle()
        } else {
            shapeBackgroundColor = Color.WHITE
            iconsColor = Color.DKGRAY
            textColor = currentTextColor
            hintTextColor = currentHintTextColor
        }

        // Check if SearchManager is correctly set from AndroidManifest.xml.
        val searchManager = context.getSystemService(Context.SEARCH_SERVICE) as? SearchManager
        if (searchManager != null) {
            setSearchableInfo(searchManager.getSearchableInfo((context as Activity).componentName))
        } else {
            Log.w(
                "If you want to enable vocal search in your Activity, " +
                        "check the javadoc of ${FloatingSearchView::class.java.name}"
            )
        }
    }

    fun handleResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQ_CODE_SPEECH_INPUT && resultCode == Activity.RESULT_OK && data != null) {
            // Get result returned from Google voice dialog.
            val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            result.firstOrNull()?.let { query ->
                // Update the query in the FloatingSearchView.
                setQuery(query, true)
            }
        }
    }

    private fun setColorFilterOnChildren(view: ViewGroup, @ColorInt color: Int) {
        (0 until view.childCount)
            .map { view.getChildAt(it) }
            .forEach {
                when (it) {
                    is ImageView -> it.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
                    is ViewGroup -> setColorFilterOnChildren(it, color)
                }
            }
    }

    private fun openVoiceDialog() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            putExtra(
                RecognizerIntent.EXTRA_PROMPT,
                context.getString(R.string.search_voice_title)
            )
        }
        try {
            // An instance of Activity is needed to use the Google voice dialog.
            (context as? Activity)?.startActivityForResult(intent, REQ_CODE_SPEECH_INPUT)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(context, R.string.search_voice_error_msg, Toast.LENGTH_LONG).show()
        }
    }

    companion object {
        private const val REQ_CODE_SPEECH_INPUT = 89
    }
}