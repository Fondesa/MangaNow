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

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fondesa.domain.latest.LatestList
import com.fondesa.domain.manga.model.Manga
import com.fondesa.manganow.R
import dagger.android.support.DaggerFragment

class LatestFragment : DaggerFragment(), LatestContract.View {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.screen_latest, container, false)

    override fun showProgressIndicator() {
        TODO("not implemented")
    }

    override fun hideProgressIndicator() {
        TODO("not implemented")
    }

    override fun showListContainer() {
        TODO("not implemented")
    }

    override fun hideListContainer() {
        TODO("not implemented")
    }

    override fun showErrorMessage(msg: String) {
        TODO("not implemented")
    }

    override fun updateLatestList(latest: LatestList) {
        TODO("not implemented")
    }

    override fun updatePageSize(pageSize: Int) {
        TODO("not implemented")
    }

    override fun navigateToDetailScreen(manga: Manga) {
        TODO("not implemented")
    }

}