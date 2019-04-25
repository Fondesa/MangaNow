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

package com.fondesa.manganow.splash.impl

import com.fondesa.manganow.ui.api.mvp.BasePresenter

/**
 * Used to specify the contract between the view and the presenter for the splash screen section.
 */
object SplashContract {

    /**
     * Specify the view methods for the splash screen section.
     */
    interface View {

        /**
         * Called when a progress indicator of an asynchronous operation must be shown.
         */
        fun showProgressIndicator()

        /**
         * Called when a progress indicator of an asynchronous operation must be hidden.
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
         * @param cause the cause of the error.
         */
        fun showErrorForCause(cause: ErrorCause)

        /**
         * Called when the error message previously shown to the user must disappear from the screen.
         */
        fun hideErrorMessage()

        /**
         * Called when the navigation must proceed to the the main screen of the app.
         */
        fun navigateToMainScreen()
    }

    /**
     * Specify the presenter methods for the splash screen section linked to [View].
     */
    interface Presenter : BasePresenter {

        fun retryButtonClicked()


//        /**
//         * Locks the navigation to the main section.
//         */
//        fun lockNavigation()
//
//        /**
//         * Unlocks the navigation to the main section.
//         */
//        fun unlockNavigation()
    }
}