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

package com.fondesa.database.api.clause

/**
 * Type of [Expression] used to concatenate an array of [Expression]s with a keyword.
 */
abstract class MergeExpression internal constructor() : Expression {

    final override fun raw(): String {
        val expressions = expressions()
        val mergeText = mergeText()
        return expressions.joinToString(mergeText) {
            // Add brackets to avoid errors with nested expressions.
            "(${it.raw()})"
        }
    }

    override fun args(): Array<out String> = expressionArgs(this)

    private fun expressionArgs(expression: Expression): Array<out String> {
        val args = mutableListOf<String>()
        if (expression is MergeExpression) {
            // Add all the arguments of the sub-expressions to the same array.
            val mergedArgs = expression.expressions().flatMap { expressionArgs(it).toList() }
            args.addAll(mergedArgs)
        } else if (expression is ColumnExpression) {
            // Add all the arguments of the single column expressions.
            args.addAll(expression.args())
        }
        return args.toTypedArray()
    }

    /**
     * Define the [Expression]s that must be concatenated.
     *
     * @return array of [Expression].
     */
    abstract fun expressions(): Array<out Expression>

    /**
     * Define the text used to interpolate the [Expression].
     * E.g. (exp1)$mergeText(exp2)
     *
     * @return text between [Expression]s.
     */
    abstract fun mergeText(): String
}

