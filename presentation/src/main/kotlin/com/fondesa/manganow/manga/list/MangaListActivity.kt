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

package com.fondesa.manganow.manga.list

import android.os.Bundle
import android.widget.Toast
import com.fondesa.data.remote.connectivity.ConnectivityManager
import com.fondesa.manganow.R
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class MangaListActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var connectivityManager: ConnectivityManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Toast.makeText(this, "${connectivityManager.isConnected()}", Toast.LENGTH_SHORT).show()
    }
}