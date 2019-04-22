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

package com.fondesa.manganow.latest

import com.fondesa.domain.latest.LatestList
import com.fondesa.domain.latest.model.Latest
import com.fondesa.manganow.ui.mvp.BasePresenter

/**
 * Used to specify the contract between the view and the presenter for the latest section.
 */
object LatestContract {

    const val PAGE_SIZE = 25

    /**
     * Specify the view methods for the latest section.
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
         * Called when the container of the list showing the [Latest] objects must be shown or hidden.
         */
        fun showListContainer()

        /**
         * Called when the container of the list showing the [Latest] objects must be shown or hidden.
         */
        fun hideListContainer()

        /**
         * Called when an error occurs and the readable explanation of this message must
         * be shown to the user.
         *
         * @param msg message that must be shown to the user.
         */
        fun showErrorMessage(msg: String)

        /**
         * Called when a new list of [Latest] objects is available.
         * The UI must be updated with new [Latest] objects.
         *
         * @param latest new list of [Latest] objects.
         */
        fun updateLatestList(latest: LatestList)
    }

    /**
     * Specify the presenter methods for the latest section linked to [View].
     */
    interface Presenter : BasePresenter<View> {

        /**
         * Used to notify the presenter when the user reaches the end of the page.
         */
        fun pageEnded()

        /**
         * Called when the user selects a [Latest] in the list.
         *
         * @param latest item of type [Latest] selected by the user.
         */
        fun latestSelected(latest: Latest)
    }
}