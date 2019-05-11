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

package com.fondesa.manganow.core.impl

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import com.fondesa.manganow.core.api.ActivityLifecycleCallbacks
import com.fondesa.manganow.core.api.ActivityRetriever
import com.fondesa.manganow.core.api.AppInitializer
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ActivityRetrieverImpl @Inject constructor(
    private val context: Context
) : ActivityRetriever,
    AppInitializer,
    ActivityLifecycleCallbacks {

    private val activities = mutableListOf<Activity>()

    override fun initialize() {
        (context.applicationContext as Application).registerActivityLifecycleCallbacks(this)
    }

    override fun retrieveCurrent(): Activity? = activities.lastOrNull()

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        activities.add(activity)
    }

    override fun onActivityDestroyed(activity: Activity) {
        activities.remove(activity)
    }
}