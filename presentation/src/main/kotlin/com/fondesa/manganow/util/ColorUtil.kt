package com.fondesa.manganow.util

import android.content.Context
import android.support.annotation.AttrRes
import android.support.annotation.ColorInt
import android.util.TypedValue
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