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

import android.support.annotation.LayoutRes
import com.fondesa.domain.latest.LatestList
import com.fondesa.domain.manga.model.Manga
import com.fondesa.manganow.R
import com.fondesa.manganow.fragment.DrawerFragment

class LatestFragment : DrawerFragment(), LatestContract.View {

    @LayoutRes
    override val contentLayout: Int = R.layout.screen_latest

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