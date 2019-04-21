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

package com.fondesa.database.api.statement.base

import android.database.sqlite.SQLiteDatabase
import com.fondesa.database.api.execution.Executor
import com.fondesa.database.api.statement.Statement

/**
 * Define a [Statement] that will receive the [SQLiteDatabase] instance.
 * The [SQLiteDatabase] instance will be available during the compile phase of the [Statement].
 */
abstract class SQLiteBaseStatement<out E : Executor<*>> : Statement<E>,
    SQLiteDatabaseReceiver {

    /**
     * Instance of [SQLiteDatabase] injected by another component.
     */
    protected val database: SQLiteDatabase
        get() = _database ?: throw NullPointerException("Database is not injected.")

    private var _database: SQLiteDatabase? = null

    final override fun injectDatabase(database: SQLiteDatabase) {
        _database = database
    }
}