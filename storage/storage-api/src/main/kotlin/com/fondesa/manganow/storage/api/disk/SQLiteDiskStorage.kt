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

package com.fondesa.manganow.storage.api.disk

import com.fondesa.manganow.database.api.client.DatabaseClient
import com.fondesa.manganow.database.api.client.clause.ConflictType
import com.fondesa.manganow.database.api.client.extension.and
import com.fondesa.manganow.database.api.client.extension.equalTo
import com.fondesa.manganow.database.api.client.extension.majorThan
import com.fondesa.manganow.database.api.client.statement.Insert
import com.fondesa.manganow.database.api.client.statement.Select

abstract class SQLiteDiskStorage<T>(
    client: DatabaseClient,
    private val cacheKey: String,
    private val expirationTimeMs: Long
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
        val count = database.compile(Statements.countCache(cacheKey, validDate))
            .execute()
            .simpleInteger()
        return count > 0
    }

    private fun cacheRecordId(): Long {
        return database.compile(Statements.selectCache(cacheKey))
            .execute()
            .firstOrNull {
                // Get the record's id.
                it.getLong(CacheTable.COL_ID)
            }
            ?: throw NullPointerException("Cache id for the identifier `$cacheKey` not found")
    }

    private fun insertCacheRecord(): Long = database.compile(Statements.insertCache())
        .bindNull(CacheTable.COL_ID)
        .bindLong(CacheTable.COL_DATE_MS, System.currentTimeMillis())
        .bindString(CacheTable.COL_KEY, cacheKey)
        .execute(close = true)

    private object Statements {

        fun countCache(remoteTaskPath: String, validDate: Long) = Select.from(CacheTable.NAME)
            .count()
            .where(
                CacheTable.COL_KEY.equalTo(remoteTaskPath) and
                        CacheTable.COL_DATE_MS.majorThan(validDate)
            )
            .build()

        fun selectCache(remoteTaskPath: String) = Select.from(CacheTable.NAME)
            .columns(CacheTable.COL_ID)
            .where(CacheTable.COL_KEY.equalTo(remoteTaskPath))
            .limit(1)
            .build()

        fun insertCache() = Insert.into(CacheTable.NAME)
            .conflictType(ConflictType.REPLACE)
            .columns(
                CacheTable.COL_ID,
                CacheTable.COL_DATE_MS,
                CacheTable.COL_KEY
            )
            .build()
    }
}