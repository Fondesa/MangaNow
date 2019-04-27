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
import android.view.View
import androidx.lifecycle.LifecycleObserver
import androidx.recyclerview.widget.DividerItemDecoration
import com.fondesa.manganow.latest.impl.qualifiers.PageSize
import com.fondesa.manganow.ui.api.BaseActivity
import com.fondesa.manganow.ui.api.NavigationActivityViewDelegate
import com.fondesa.manganow.ui.api.util.addObservers
import com.fondesa.manganow.ui.api.view.RecyclerViewScrollEndedListener
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_latest.*
import javax.inject.Inject

class LatestActivity : BaseActivity<NavigationActivityViewDelegate>(),
    LatestContract.View,
    LatestRecyclerViewAdapter.OnLatestClickListener {

    @Inject
    internal lateinit var presenter: LatestContract.Presenter

    @Inject
    internal lateinit var lifecycleObservers: Set<@JvmSuppressWildcards LifecycleObserver>

    @Inject
    internal lateinit var adapter: LatestRecyclerViewAdapter

    @JvmField
    @field:[Inject PageSize]
    internal var pageSize: Int = 0

    override fun onViewCreated(savedInstanceState: Bundle?) {
        // Add all the lifecycle observers
        lifecycle.addObservers(lifecycleObservers)
        // Add the listener for the pagination.
        val paginationListener = RecyclerViewScrollEndedListener(threshold = LIST_THRESHOLD) {
            presenter.pageEnded()
        }
        recyclerView.addOnScrollListener(paginationListener)
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        // Set the adapter on the RecyclerView.
        recyclerView.adapter = adapter
        // Attach the view to the presenter.
        presenter.attach()
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

    override fun showErrorForCause(cause: ErrorCause) {
        Snackbar.make(viewManager.coordinatorLayout, cause.toErrorMessage(), Snackbar.LENGTH_LONG)
            .show()
    }

    override fun showLatestManga(latest: LatestList) {
        adapter.updateList(latest)
    }

    override fun onLatestClicked(latest: Latest) {
        presenter.latestClicked(latest)
    }

    companion object {

        private const val LIST_THRESHOLD = 3
    }
}