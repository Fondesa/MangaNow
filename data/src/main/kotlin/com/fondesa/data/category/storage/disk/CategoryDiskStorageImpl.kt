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

package com.fondesa.data.category.storage.disk

import com.fondesa.data.category.database.CategoryTable
import com.fondesa.data.storage.disk.SQLiteDiskStorage
import com.fondesa.domain.category.CategoryList
import com.fondesa.domain.category.model.Category
import com.fondesa.manganow.database.api.DatabaseClient
import com.fondesa.manganow.database.api.clause.ConflictType
import com.fondesa.manganow.database.api.statement.Insert
import com.fondesa.manganow.database.api.statement.Select

class CategoryDiskStorageImpl(
    client: DatabaseClient,
    expirationTimeMs: Long,
    remoteTaskKey: String
) : SQLiteDiskStorage<CategoryList>(client, expirationTimeMs, remoteTaskKey) {

    override fun get(cacheId: Long): CategoryList =
        database.compile(Statements.selectCategories())
            .execute()
            .map {
                Category(
                    id = it.getLong(CategoryTable.COL_ID),
                    name = it.getString(CategoryTable.COL_NAME)
                )
            }

    override fun put(cacheId: Long, item: CategoryList) {
        val categoryExecutor = database.compile(Statements.insertCategory())
        item.forEach {
            categoryExecutor.bindLong(CategoryTable.COL_ID, it.id)
                .bindString(CategoryTable.COL_NAME, it.name)
                .execute()
        }
        categoryExecutor.close()
    }

    private object Statements {

        fun selectCategories() = Select.from(CategoryTable.NAME)
            .columns(
                CategoryTable.COL_ID,
                CategoryTable.COL_NAME
            )
            .build()

        fun insertCategory() = Insert.into(CategoryTable.NAME)
            .conflictType(ConflictType.REPLACE)
            .columns(
                CategoryTable.COL_ID,
                CategoryTable.COL_NAME
            )
            .build()
    }
}