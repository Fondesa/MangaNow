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

package com.fondesa.data.category.cache

import com.fondesa.data.cache.SQLiteCache
import com.fondesa.data.category.database.CategoryTable
import com.fondesa.database.DatabaseClient
import com.fondesa.database.clause.ConflictType
import com.fondesa.database.statement.Insert
import com.fondesa.database.statement.Select
import com.fondesa.domain.category.CategoryList
import com.fondesa.domain.category.model.Category
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class CategoryCache @Inject constructor(client: DatabaseClient) :
    SQLiteCache<CategoryList>(client) {

    override val expirationTimeMs: Long = TimeUnit.DAYS.toMillis(7)

    override val remoteTaskPath: String = "categories"

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