/*
 * Copyright (c) 2019 Fondesa
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

package com.fondesa.manganow.database.api.clause

import com.fondesa.manganow.database.api.structure.Column

/**
 * Type of [Expression] that adds a raw text to the name of a [Column].
 * This [Expression] is used for the most popular SQLite expressions.
 * E.g. >, <, IS NULL, IS NOT NULL, etc..
 *
 * @param column instance of [Column].
 * @param raw raw text that will be added after the name of [column].
 * @param args the arguments that will be bound in the compiled statement.
 */
class ColumnExpression(val column: Column<*>, val raw: String, vararg val args: String) :
    Expression {

    override fun raw(): String = column.fullName + raw

    override fun args(): Array<out String> = args
}