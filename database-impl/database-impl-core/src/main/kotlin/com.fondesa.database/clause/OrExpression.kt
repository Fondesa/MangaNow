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
 * Concatenate [Expression]s with the OR keyword.
 * The [Expression] parameters are repeated to have an array with at least 2 [Expression]s.
 *
 * @param first first [Expression].
 * @param second second [Expression].
 * @param others other [Expression]s that will be concatenated to the first two.
 */
class OrExpression(first: Expression, second: Expression, vararg others: Expression) :
    MergeExpression() {

    private val expressions = arrayOf(first, second).plus(others)

    override fun expressions(): Array<out Expression> = expressions

    override fun mergeText(): String = "OR"
}