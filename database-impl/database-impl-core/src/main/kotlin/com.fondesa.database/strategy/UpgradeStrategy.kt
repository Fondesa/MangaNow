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

package com.fondesa.database.strategy

import com.fondesa.database.api.Database
import com.fondesa.database.api.structure.Graph

/**
 * Used to define actions to take when the schema's version is increased.
 * The methods of the [UpgradeStrategy] are invoked at maximum once after the database creation.
 */
interface UpgradeStrategy {

    /**
     * Migrate the schema when the version of the database increases.
     * This method runs in a database transaction.
     *
     * @param database current instance of [Database].
     * @param oldVersion previous version of the schema.
     * @param newVersion new version of the schema.
     * @param newGraph graph related to the new schema's version.
     */
    fun onUpgrade(database: Database, oldVersion: Int, newVersion: Int, newGraph: Graph)

    /**
     * Useful to finalize the upgrade outside of a transaction.
     * If [onUpgrade] wasn't called, this method won't be called.
     *
     * @param database current instance of [Database].
     * @param oldVersion previous version of the schema.
     * @param newVersion new version of the schema.
     * @param newGraph graph related to the new schema's version.
     */
    fun onPostUpgrade(database: Database, oldVersion: Int, newVersion: Int, newGraph: Graph)
}