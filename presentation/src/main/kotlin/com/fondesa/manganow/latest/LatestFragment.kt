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
import android.support.annotation.LayoutRes
import android.support.design.widget.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fondesa.domain.latest.LatestList
import com.fondesa.domain.latest.model.Latest
import com.fondesa.manganow.R
import com.fondesa.manganow.fragment.DrawerFragment
import com.fondesa.manganow.view.RecyclerViewScrollEndedListener
import com.fondesa.recyclerviewdivider.RecyclerViewDivider
import kotlinx.android.synthetic.main.screen_base_drawer.*
import kotlinx.android.synthetic.main.screen_latest.*
import javax.inject.Inject

class LatestFragment : DrawerFragment(), LatestContract.View,
    LatestRecyclerViewAdapter.OnLatestClickListener {

    @Inject
    lateinit var presenter: LatestContract.Presenter

    @LayoutRes
    override val contentLayout: Int = R.layout.screen_latest

    private val listAdapter by lazy { LatestRecyclerViewAdapter() }
    private val scrollEndedListener = RecyclerViewScrollEndedListener {
        presenter.pageEnded()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        activity?.setTitle(R.string.section_home)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let {
            // Add the divider.
            RecyclerViewDivider.with(it).build().addTo(recyclerView)
        }
        recyclerView.addOnScrollListener(scrollEndedListener)
        listAdapter.clickListener = this
        // Set the adapter on the RecyclerView.
        recyclerView.adapter = listAdapter
        // Attach the view to the presenter.
        presenter.attachView(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        listAdapter.clickListener = null
        recyclerView.removeOnScrollListener(scrollEndedListener)
        // Detach the view from the presenter.
        presenter.detachView()
    }

    override fun showProgressIndicator() {
        progressBar.visibility = View.VISIBLE
    }

    override fun hideProgressIndicator() {
        progressBar.visibility = View.INVISIBLE
    }

    override fun showListContainer() {
        recyclerView.visibility = View.VISIBLE
    }

    override fun hideListContainer() {
        recyclerView.visibility = View.INVISIBLE
    }

    override fun showErrorMessage(msg: String) {
        Snackbar.make(coordinator, msg, Snackbar.LENGTH_LONG).show()
    }

    override fun updateLatestList(latest: LatestList) {
        listAdapter.updateList(latest)
    }

    override fun onLatestClicked(latest: Latest) {
        presenter.latestSelected(latest)
    }
}