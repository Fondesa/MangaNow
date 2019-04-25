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

package com.fondesa.manganow.ui.api.mvp

/**
 * Defines the presenter in an MVP pattern.
 * The presenter is used to define the logic between the model and the view.
 * It mustn't have any relationship with the Android code to be tested with jUnit tests.
 * The presenter can manage only one view.
 */
interface BasePresenter {

    /**
     * Attach the view to the presenter.
     * The view must be attached to be updated.
     */
    fun attach()

    /**
     * Detach the view from the presenter and release any resource related to it.
     * The view can't be updated after the detach.
     */
    fun detach()
}