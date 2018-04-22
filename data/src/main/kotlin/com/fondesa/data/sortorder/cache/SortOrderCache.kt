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

package com.fondesa.data.sortorder.cache

import com.fondesa.data.cache.SQLiteCache
import com.fondesa.data.sortorder.database.SortOrderTable
import com.fondesa.database.DatabaseClient
import com.fondesa.database.clause.ConflictType
import com.fondesa.database.statement.Insert
import com.fondesa.database.statement.Select
import com.fondesa.domain.sortorder.SortOrderList
import com.fondesa.domain.sortorder.model.SortOrder
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SortOrderCache @Inject constructor(client: DatabaseClient) :
    SQLiteCache<SortOrderList>(client) {

    override val expirationTimeMs: Long = TimeUnit.DAYS.toMillis(7)

    override val remoteTaskPath: String = "sort_orders"

    override fun get(cacheId: Long): SortOrderList =
        database.compile(Statements.selectSortOrders())
            .execute()
            .map {
                SortOrder(
                    id = it.getLong(SortOrderTable.COL_ID),
                    name = it.getString(SortOrderTable.COL_NAME),
                    priority = it.getInt(SortOrderTable.COL_PRIORITY)
                )
            }

    override fun put(cacheId: Long, item: SortOrderList) {
        val sortExecutor = database.compile(Statements.insertSortOrder())
        item.forEach {
            sortExecutor.bindLong(SortOrderTable.COL_ID, it.id)
                .bindString(SortOrderTable.COL_NAME, it.name)
                .bindInt(SortOrderTable.COL_PRIORITY, it.priority)
                .execute()
        }
        sortExecutor.close()
    }

    private object Statements {

        fun selectSortOrders() = Select.from(SortOrderTable.NAME)
            .columns(
                SortOrderTable.COL_ID,
                SortOrderTable.COL_NAME,
                SortOrderTable.COL_PRIORITY
            )
            .orderAsc(SortOrderTable.COL_PRIORITY)
            .build()

        fun insertSortOrder() = Insert.into(SortOrderTable.NAME)
            .conflictType(ConflictType.REPLACE)
            .columns(
                SortOrderTable.COL_ID,
                SortOrderTable.COL_NAME,
                SortOrderTable.COL_PRIORITY
            )
            .build()
    }
}