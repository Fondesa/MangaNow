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

package com.fondesa.data.cache

import com.fondesa.data.database.table.RemoteTaskCacheTable
import com.fondesa.database.DatabaseClient
import com.fondesa.database.clause.ConflictType
import com.fondesa.database.extension.and
import com.fondesa.database.extension.equalTo
import com.fondesa.database.extension.majorThan
import com.fondesa.database.statement.Insert
import com.fondesa.database.statement.Select
import javax.inject.Inject

abstract class SQLiteCache<T> @Inject constructor(client: DatabaseClient) : Cache<T> {

    protected val database by lazy { client.getDatabase() }

    protected abstract val expirationTimeMs: Long

    protected abstract val remoteTaskPath: String

    protected abstract fun get(cacheId: Long): T

    protected abstract fun put(cacheId: Long, item: T)

    final override fun get(): T {
        val cacheId = cacheRecordId()
        // Get the item related to this cache id.
        return get(cacheId)
    }

    final override fun put(item: T) {
        // Insert all records in a single transaction.
        database.transaction {
            // Insert cache record.
            val cacheId = insertCacheRecord()
            // Insert the records.
            put(cacheId, item)
        }
    }

    override fun isValid(): Boolean {
        // After the expiration time the cache will be invalidated.
        val validDate = System.currentTimeMillis() - expirationTimeMs

        // If there's at least one record in cache inserted in an acceptable expiration time and
        // with the same path, the cache is valid.
        val count = database.compile(Statements.countCache(remoteTaskPath, validDate))
            .execute()
            .simpleInteger()
        return count > 0
    }

    private fun cacheRecordId(): Long {
        return database.compile(Statements.selectCache(remoteTaskPath))
            .execute()
            .firstOrNull {
                // Get the record's id.
                it.getLong(RemoteTaskCacheTable.COL_ID)
            } ?: throw NullPointerException("Cache id for the path `$remoteTaskPath` not found")
    }

    private fun insertCacheRecord(): Long = database.compile(Statements.insertCache())
        .bindNull(RemoteTaskCacheTable.COL_ID)
        .bindLong(RemoteTaskCacheTable.COL_DATE_MS, System.currentTimeMillis())
        .bindString(RemoteTaskCacheTable.COL_PATH, remoteTaskPath)
        .execute(close = true)

    private object Statements {

        fun countCache(remoteTaskPath: String, validDate: Long) =
            Select.from(RemoteTaskCacheTable.NAME)
                .count()
                .where(RemoteTaskCacheTable.COL_PATH.equalTo(remoteTaskPath) and
                        RemoteTaskCacheTable.COL_DATE_MS.majorThan(validDate))
                .build()

        fun selectCache(remoteTaskPath: String) = Select.from(RemoteTaskCacheTable.NAME)
            .columns(RemoteTaskCacheTable.COL_ID)
            .where(RemoteTaskCacheTable.COL_PATH.equalTo(remoteTaskPath))
            .limit(1)
            .build()

        fun insertCache() = Insert.into(RemoteTaskCacheTable.NAME)
            .conflictType(ConflictType.REPLACE)
            .columns(
                RemoteTaskCacheTable.COL_ID,
                RemoteTaskCacheTable.COL_DATE_MS,
                RemoteTaskCacheTable.COL_PATH
            )
            .build()
    }
}