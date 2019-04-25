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

package com.fondesa.data.sortorder.storage.disk

import com.fondesa.data.sortorder.database.SortOrderTable
import com.fondesa.domain.sortorder.SortOrderList
import com.fondesa.domain.sortorder.model.SortOrder
import com.fondesa.manganow.storage.api.disk.SQLiteDiskStorage

class SortOrderDiskStorageImpl(
    client: com.fondesa.manganow.database.api.client.DatabaseClient,
    expirationTimeMs: Long,
    remoteTaskKey: String
) : SQLiteDiskStorage<SortOrderList>(client, expirationTimeMs, remoteTaskKey) {

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

        fun selectSortOrders() = com.fondesa.manganow.database.api.client.statement.Select.from(SortOrderTable.NAME)
            .columns(
                SortOrderTable.COL_ID,
                SortOrderTable.COL_NAME,
                SortOrderTable.COL_PRIORITY
            )
            .orderAsc(SortOrderTable.COL_PRIORITY)
            .build()

        fun insertSortOrder() = com.fondesa.manganow.database.api.client.statement.Insert.into(SortOrderTable.NAME)
            .conflictType(com.fondesa.manganow.database.api.client.clause.ConflictType.REPLACE)
            .columns(
                SortOrderTable.COL_ID,
                SortOrderTable.COL_NAME,
                SortOrderTable.COL_PRIORITY
            )
            .build()
    }
}