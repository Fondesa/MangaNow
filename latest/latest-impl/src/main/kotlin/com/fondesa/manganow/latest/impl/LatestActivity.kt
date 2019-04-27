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

import android.os.Bundle
import com.fondesa.manganow.log.api.Log
import com.fondesa.manganow.ui.api.BaseActivity
import com.fondesa.manganow.ui.api.NavigationActivityViewDelegate
import javax.inject.Inject

class LatestActivity : BaseActivity<NavigationActivityViewDelegate>(),
    LatestRecyclerViewAdapter.OnLatestClickListener {

    @Inject
    internal lateinit var adapter: LatestRecyclerViewAdapter

    override fun onViewCreated(savedInstanceState: Bundle?) {
        Log.d("LYRA")
    }

    override fun onLatestClicked(latest: Latest) {
        TODO("not implemented")
    }
}