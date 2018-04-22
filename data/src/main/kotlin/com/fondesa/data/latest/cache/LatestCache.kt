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

package com.fondesa.data.latest.cache

import com.fondesa.data.cache.SQLiteCache
import com.fondesa.database.DatabaseClient
import com.fondesa.domain.latest.LatestList
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class LatestCache @Inject constructor(client: DatabaseClient) :
    SQLiteCache<LatestList>(client) {

    override val expirationTimeMs: Long = TimeUnit.MINUTES.toMillis(5)

    override val remoteTaskPath: String = TODO()

    override fun get(cacheId: Long): LatestList {
        TODO()
    }

    override fun put(cacheId: Long, item: LatestList) {
        TODO()
    }

    private object Statements {

    }
}