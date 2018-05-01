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

package com.fondesa.manganow.extension

import android.support.annotation.IdRes
import android.support.design.widget.NavigationView
import android.view.View

@IdRes
fun NavigationView.getCheckedItem(): Int {
    val menu = menu
    return (0 until menu.size()).map {
        menu.getItem(it)
    }.firstOrNull {
        it.isChecked
    }?.itemId ?: View.NO_ID
}