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

import com.fondesa.database.api.clause.Expression

/**
 * Raw SQLite expression with the arguments that will be bound in the compiled statement.
 *
 * @param raw the raw text that expressing the query.
 * @param args the arguments that will be bound in the compiled statement.
 */
class RawExpression(val raw: String, vararg val args: String) :
    Expression {

    override fun raw(): String = raw

    override fun args(): Array<out String> = args
}