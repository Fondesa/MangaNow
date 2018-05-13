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

package com.fondesa.manganow.splash

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fondesa.manganow.R
import com.fondesa.manganow.fragment.AdditionalNavigationArgumentsProvider
import com.fondesa.manganow.fragment.DrawerFragment
import com.fondesa.manganow.util.ColorUtil
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class SplashFragment : DaggerFragment(),
    SplashContract.View,
    AdditionalNavigationArgumentsProvider {

    @Inject
    lateinit var presenter: SplashContract.Presenter

    private var originalStatusBarColor: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.screen_splash, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Attach the view to the presenter.
        presenter.attachView(this)
        // Set the click listener.
        retryButton.setOnClickListener {
            // Retry to load the data.
            presenter.retryButtonClicked()
        }

        val activity = activity
        if (activity != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Store the current status bar color to restore it later.
            originalStatusBarColor = activity.window?.statusBarColor
            // Change the status bar color.
            activity.window?.statusBarColor = ColorUtil.getPrimaryDark(activity)
        }
    }

    override fun onResume() {
        super.onResume()
        // Allow the navigation when the app comes to foreground.
        presenter.movedToForeground()
    }

    override fun onPause() {
        super.onPause()
        // Disallow the navigation if the app goes to create.
        presenter.movedToBackground()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Detach the view from the presenter.
        presenter.detachView()

        val activity = activity
        val originalStatusBarColor = originalStatusBarColor
        if (activity != null &&
            originalStatusBarColor != null &&
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
        ) {
            // Restore the previous status bar color.
            activity.window?.statusBarColor = originalStatusBarColor
        }
    }

    override fun showProgressIndicator() {
        progressBar.visibility = View.VISIBLE
    }

    override fun hideProgressIndicator() {
        progressBar.visibility = View.INVISIBLE
    }

    override fun showRetryButton() {
        retryButton.visibility = View.VISIBLE
    }

    override fun hideRetryButton() {
        retryButton.visibility = View.INVISIBLE
    }

    override fun showErrorMessage(msg: String) {
        errorTextView.text = msg
        errorTextView.visibility = View.VISIBLE
    }

    override fun hideErrorMessage() {
        errorTextView.visibility = View.INVISIBLE
    }

    override fun provideAdditionalArguments(): Bundle =
        DrawerFragment.drawerItemBundle(R.id.section_home)
}