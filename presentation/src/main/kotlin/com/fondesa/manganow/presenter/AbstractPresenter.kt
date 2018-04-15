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

package com.fondesa.manganow.presenter

/**
 * Base implementation of the presenter used to hold the reference to the view.
 * If the view is accessed when it is detached, a [NullPointerException] will be thrown.
 */
abstract class AbstractPresenter<V> : BasePresenter<V> {

    /**
     * View used in this presenter.
     * The view is available only between [attachView] and [detachView].
     * In the case the View is accessed outside this range, a [NullPointerException] will be thrown.
     */
    protected val view: V
        get() {
            // Check if the view is attached.
            if (isViewAttached())
                return _view!!

            throw ViewNotAttachedException()
        }

    // Backing property of view.
    private var _view: V? = null

    override fun attachView(view: V) {
        _view = view
    }

    final override fun isViewAttached(): Boolean = _view != null

    override fun detachView() {
        _view = null
    }
}