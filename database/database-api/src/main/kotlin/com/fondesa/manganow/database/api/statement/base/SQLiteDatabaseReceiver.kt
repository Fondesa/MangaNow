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

package com.fondesa.manganow.database.api.statement.base

import android.database.sqlite.SQLiteDatabase
import com.fondesa.manganow.database.api.Database

/**
 * Used to receive a [SQLiteDatabase] instance.
 * The [SQLiteDatabase] instance must be provided and injected by another component (e.g. [Database]).
 */
interface SQLiteDatabaseReceiver {

    /**
     * Get the [SQLiteDatabase] instance injected by another component.
     *
     * @param database [SQLiteDatabase] used to compile the statement.
     */
    fun injectDatabase(database: SQLiteDatabase)
}