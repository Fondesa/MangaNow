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

package com.fondesa.data.database

/**
 * Used to manage the creation and the access to a [Database].
 * The close method is not provided to have the connection always opened.
 * In this way, the database can be accessed directly without checking if it's opened or closed.
 */
interface DatabaseClient {

    /**
     * Creates the [Database] and opens the connection to it.
     */
    fun createDatabase()

    /**
     * Get the [Database] instance created through [createDatabase].
     * The [Database] connection must be opened in order to use it.
     */
    fun getDatabase(): Database
}