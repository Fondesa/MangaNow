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

package com.fondesa.manganow.latest.impl

import android.app.Activity
import android.content.Intent
import com.fondesa.manganow.latest.api.LatestRoute
import com.fondesa.manganow.navigation.api.TypedRouteConsumer
import javax.inject.Inject

class LatestRouteConsumer @Inject constructor(private val activity: Activity) :
    TypedRouteConsumer<LatestRoute>(LatestRoute::class) {

    override fun consumeRoute(route: LatestRoute) {
        val intent = Intent(activity, LatestActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        activity.startActivity(intent)
    }
}