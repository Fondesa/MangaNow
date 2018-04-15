package com.fondesa.manganow.view

import android.support.v7.app.AppCompatActivity
import android.view.ViewGroup

/**
 * Manages the creation of the main view of an [AppCompatActivity].
 * The created view can be also customized through [bind].
 */
interface ViewManager {

    /**
     * Creates the root view of the [AppCompatActivity].
     * The root view must be a [ViewGroup].
     * The created view will be set as content view automatically.
     *
     * @param activity [AppCompatActivity] in which the view must be created.
     * @return view that must be set as content view.
     */
    fun createRootView(activity: AppCompatActivity): ViewGroup

    /**
     * Customize the properties of the created view.
     *
     * @param activity [AppCompatActivity] in which the view was created.
     * @param rootView previously created [ViewGroup] with [createRootView].
     */
    fun bind(activity: AppCompatActivity, rootView: ViewGroup)

    /**
     * Release any resource related to the created [ViewGroup].
     *
     * @param activity [AppCompatActivity] in which the view was created.
     * @param rootView previously created [ViewGroup] with [createRootView].
     */
    fun detach(activity: AppCompatActivity, rootView: ViewGroup)
}