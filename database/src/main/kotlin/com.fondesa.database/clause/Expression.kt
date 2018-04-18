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
 * Define a SQLite expression like WHERE, HAVING, ON, etc..
 */
interface Expression {

    /**
     * Get the raw statement representing the expression.
     * The raw statement mustn't contain the clause keyword (e.g. WHERE, HAVING, ON).
     *
     * @return raw SQL expression.
     */
    fun raw(): String

    /**
     * The arguments that will be bound into the compiled statement.
     * The usage of arguments is strongly encouraged to avoid SQL injection attacks.
     *
     * @return array of arguments that will be bound as strings inside the compiled statement.
     */
    fun args(): Array<out String>
}