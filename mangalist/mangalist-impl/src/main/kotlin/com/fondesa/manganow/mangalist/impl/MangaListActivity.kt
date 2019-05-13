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

package com.fondesa.manganow.mangalist.impl

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import androidx.lifecycle.LifecycleObserver
import androidx.recyclerview.widget.DividerItemDecoration
import com.fondesa.manganow.core.api.intentFor
import com.fondesa.manganow.domain.manga.Manga
import com.fondesa.manganow.mangalist.api.sortorder.SortOrder
import com.fondesa.manganow.mangalist.api.sortorder.SortOrderList
import com.fondesa.manganow.mangalist.impl.qualifiers.PageSize
import com.fondesa.manganow.mangalist.impl.sortorder.SortOrderSpinnerAdapter
import com.fondesa.manganow.ui.api.BaseActivity
import com.fondesa.manganow.ui.api.NavigationActivityViewDelegate
import com.fondesa.manganow.ui.api.util.addObservers
import com.fondesa.manganow.ui.api.view.AutoSubmitQueryTextListener
import com.fondesa.manganow.ui.api.view.RecyclerViewScrollEndedListener
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_manga_list.*
import kotlinx.android.synthetic.main.partial_search_app_bar.*
import javax.inject.Inject

class MangaListActivity : BaseActivity<NavigationActivityViewDelegate>(),
    MangaListContract.View,
    MangaListRecyclerViewAdapter.OnMangaClickListener,
    AdapterView.OnItemSelectedListener {

    @Inject
    internal lateinit var presenter: MangaListContract.Presenter

    @Inject
    internal lateinit var lifecycleObservers: Set<@JvmSuppressWildcards LifecycleObserver>

    @Inject
    internal lateinit var mangaListAdapter: MangaListRecyclerViewAdapter

    @Inject
    internal lateinit var sortOrderAdapter: SortOrderSpinnerAdapter

    @JvmField
    @field:[Inject PageSize]
    internal var pageSize: Int = 0

    private val Menu.sortOrderMenuItem: MenuItem
        get() = findItem(R.id.action_change_sort_order)

    private val Menu.sortOrderSpinner: Spinner
        get() = sortOrderMenuItem.actionView as Spinner


    override fun onViewCreated(savedInstanceState: Bundle?) {
        // Add all the lifecycle observers
        lifecycle.addObservers(lifecycleObservers)
        // Add the listener for the pagination.
        val paginationListener = RecyclerViewScrollEndedListener(threshold = LIST_THRESHOLD) {
            presenter.pageEnded()
        }
        recyclerView.addOnScrollListener(paginationListener)
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        // Set the mangaListAdapter on the RecyclerView.
        recyclerView.adapter = mangaListAdapter

        val queryTextListener = AutoSubmitQueryTextListener {
            presenter.textSearched(it)
        }
        searchView.setOnQueryTextListener(queryTextListener)
        lifecycle.addObserver(queryTextListener)

        dispatchAfterMenuCreation {
            menuInflater.inflate(R.menu.menu_manga_list, this)
            sortOrderSpinner.adapter = sortOrderAdapter
            sortOrderSpinner.onItemSelectedListener = this@MangaListActivity
        }
        // Attach the view to the presenter.
        presenter.attach()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        searchView.handleResult(requestCode, resultCode, data)
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

    override fun showZeroElementsView() {
        zeroElementsTextView.visibility = View.VISIBLE
    }

    override fun hideZeroElementsView() {
        zeroElementsTextView.visibility = View.INVISIBLE
    }

    override fun showMangaList(mangaList: MangaList) {
        mangaListAdapter.updateList(mangaList)
    }

    override fun showSortOrdersView() = dispatchAfterMenuCreation {
        sortOrderMenuItem.isVisible = true
    }

    override fun hideSortOrdersView() = dispatchAfterMenuCreation {
        sortOrderMenuItem.isVisible = false
    }

    override fun showSortOrders(sortOrders: SortOrderList, defaultSortOrder: SortOrder) {
        sortOrderAdapter.updateList(sortOrders)
        dispatchAfterMenuCreation {
            val defaultIndex = sortOrders.indexOf(defaultSortOrder)
            if (defaultIndex == -1) {
                throw IllegalArgumentException("The sort order ${defaultSortOrder.name} can't be found in the sort order list.")
            }
            sortOrderSpinner.setSelection(defaultIndex)
        }
    }

    override fun onMangaClicked(manga: Manga) {
        presenter.mangaClicked(manga)
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        val sortOrder = sortOrderAdapter.getItem(position)
            ?: throw IllegalStateException("Can't find an item for the position $position.")
        presenter.sortOrderSelected(sortOrder)
    }

    override fun onNothingSelected(parent: AdapterView<*>) = Unit

    companion object {

        private const val LIST_THRESHOLD = 3

        fun createIntent(context: Context): Intent = intentFor<MangaListActivity>(context)
    }
}