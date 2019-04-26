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

package com.fondesa.manganow.ui.api

import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.fondesa.manganow.ui.api.navigation.Navigator
import com.google.android.material.appbar.AppBarLayout

class DefaultNavigationActivityViewDelegate(
    activity: AppCompatActivity,
    @LayoutRes private val contentLayout: Int,
    @LayoutRes private val appBarLayoutRes: Int = R.layout.partial_base_app_bar
) : BaseNavigationActivityViewDelegate(activity) {

    override fun createNavigator(): Navigator = TODO()

    override fun createAppBarLayout(): AppBarLayout {
        val appBar = View.inflate(activity, appBarLayoutRes, null) as AppBarLayout
        appBar.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        return appBar
    }

    override fun createContentLayout(): View {
        val contentLayout = View.inflate(activity, contentLayout, null)
        val layoutParams = CoordinatorLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        // The content layout uses a ScrollingViewBehavior() by default to stay below the Toolbar.
        layoutParams.behavior = AppBarLayout.ScrollingViewBehavior()
        contentLayout.layoutParams = layoutParams
        return contentLayout
    }
}