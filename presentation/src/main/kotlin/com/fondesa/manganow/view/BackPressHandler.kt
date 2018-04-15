package com.fondesa.manganow.view

import android.app.Activity

/**
 * Used to handle the back press in another component.
 */
interface BackPressHandler {

    /**
     * Manages the back press for the component that implements it.
     *
     * @return true if the back press is totally handled by this component,
     * false if after the handling done by this component, the back press must be handled
     * by the [Activity].
     */
    fun handleBackPress(): Boolean
}