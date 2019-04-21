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

package com.fondesa.data.storage.disk

import com.fondesa.data.remote.database.RemoteTaskTable
import com.fondesa.database.api.DatabaseClient
import com.fondesa.database.clause.ConflictType
import com.fondesa.database.extension.and
import com.fondesa.database.extension.equalTo
import com.fondesa.database.extension.majorThan
import com.fondesa.database.statement.Insert
import com.fondesa.database.statement.Select

abstract class SQLiteDiskStorage<T>(
    client: DatabaseClient,
    private val expirationTimeMs: Long,
    private val remoteTaskKey: String
) : DiskStorage<T> {

    protected val database by lazy { client.getDatabase() }

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
        val count = database.compile(Statements.countCache(remoteTaskKey, validDate))
            .execute()
            .simpleInteger()
        return count > 0
    }

    private fun cacheRecordId(): Long {
        return database.compile(Statements.selectCache(remoteTaskKey))
            .execute()
            .firstOrNull {
                // Get the record's id.
                it.getLong(RemoteTaskTable.COL_ID)
            }
                ?: throw NullPointerException("Cache id for the identifier `$remoteTaskKey` not found")
    }

    private fun insertCacheRecord(): Long = database.compile(Statements.insertCache())
        .bindNull(RemoteTaskTable.COL_ID)
        .bindLong(RemoteTaskTable.COL_DATE_MS, System.currentTimeMillis())
        .bindString(RemoteTaskTable.COL_KEY, remoteTaskKey)
        .execute(close = true)

    private object Statements {

        fun countCache(remoteTaskPath: String, validDate: Long) =
            Select.from(RemoteTaskTable.NAME)
                .count()
                .where(
                    RemoteTaskTable.COL_KEY.equalTo(remoteTaskPath) and
                            RemoteTaskTable.COL_DATE_MS.majorThan(validDate)
                )
                .build()

        fun selectCache(remoteTaskPath: String) = Select.from(RemoteTaskTable.NAME)
            .columns(RemoteTaskTable.COL_ID)
            .where(RemoteTaskTable.COL_KEY.equalTo(remoteTaskPath))
            .limit(1)
            .build()

        fun insertCache() = Insert.into(RemoteTaskTable.NAME)
            .conflictType(ConflictType.REPLACE)
            .columns(
                RemoteTaskTable.COL_ID,
                RemoteTaskTable.COL_DATE_MS,
                RemoteTaskTable.COL_KEY
            )
            .build()
    }
}