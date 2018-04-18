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

package com.fondesa.database.statement.base

import android.database.sqlite.SQLiteDatabase
import com.fondesa.database.Database
import com.fondesa.database.SQLiteDatabaseWrapper
import com.fondesa.database.statement.Statement

/**
 * Used to receive a [SQLiteDatabase] instance.
 * The [SQLiteDatabase] instance must be provided and injected by another component (e.g. [Database]).
 * The [SQLiteDatabaseWrapper] inject the database instance into [Statement]s by default.
 */
interface SQLiteDatabaseReceiver {

    /**
     * Get the [SQLiteDatabase] instance injected by another component.
     *
     * @param database [SQLiteDatabase] used to compile the statement.
     */
    fun injectDatabase(database: SQLiteDatabase)
}