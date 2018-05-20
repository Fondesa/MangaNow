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

import com.fondesa.manganow.presenter.BasePresenter

/**
 * Used to specify the contract between the view and the presenter for the splash screen section.
 */
object SplashContract {

    /**
     * Specify the view methods for the splash screen section.
     */
    interface View {

        /**
         * Called when a progress indicator of an asynchronous work must be shown.
         */
        fun showProgressIndicator()

        /**
         * Called when a progress indicator of an asynchronous work must be hidden.
         */
        fun hideProgressIndicator()

        /**
         * Called when the button used to retry to load the initial data must be shown.
         */
        fun showRetryButton()

        /**
         * Called when the button used to retry to load the initial data must be hidden.
         */
        fun hideRetryButton()

        /**
         * Called when an error occurs and the readable explanation of this message must
         * be shown to the user.
         *
         * @param msg message that must be shown to the user.
         */
        fun showErrorMessage(msg: String)

        /**
         * Called when the error message previously shown to the user must disappear from the screen.
         */
        fun hideErrorMessage()
    }

    /**
     * Specify the presenter methods for the splash screen section linked to [View].
     */
    interface Presenter : BasePresenter<View> {

        /**
         * Notifies the presenter when the user presses on the retry button.
         */
        fun retryButtonClicked()

        /**
         * Notifies the presenter when the view enters its foreground state.
         */
        fun movedToForeground()

        /**
         * Notifies the presenter when the view enters its background state.
         */
        fun movedToBackground()
    }
}