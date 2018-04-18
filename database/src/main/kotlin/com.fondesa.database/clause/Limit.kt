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

package com.fondesa.database.clause

/**
 * Define the LIMIT clause of SQLite.
 * The LIMIT clause is used to place an upper bound on the number of rows
 * returned by a SELECT statement or updated with an UPDATE statement.
 * It can define also an OFFSET used to define the lower bound on the number
 * of rows returned by a SELECT statement or updated with an UPDATE statement.
 */
object Limit {

    /**
     * Creates the raw LIMIT clause.
     * The default OFFSET is 0.
     *
     * @param limit upper bound on the number of rows returned or updated.
     * @param offset lower bound on the number of rows returned or updated.
     * @return raw LIMIT and OFFSET clause.
     */
    fun of(limit: Int, offset: Int = 0) = "LIMIT $limit OFFSET $offset"
}