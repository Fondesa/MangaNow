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

package com.fondesa.data.database.extension

import com.fondesa.data.database.clause.AndExpression
import com.fondesa.data.database.clause.ColumnExpression
import com.fondesa.data.database.clause.OrExpression

/**
 * Helper function used to concatenate two [ColumnExpression]s with AND.
 *
 * @param other the other [ColumnExpression] that must be concatenated to the first one.
 * @return instance of [AndExpression] with the two [ColumnExpression]s.
 */
infix fun ColumnExpression.and(other: ColumnExpression) = AndExpression(this, other)

/**
 * Helper function used to concatenate two [ColumnExpression]s with OR.
 *
 * @param other the other [ColumnExpression] that must be concatenated to the first one.
 * @return instance of [AndExpression] with the two [ColumnExpression]s.
 */
infix fun ColumnExpression.or(other: ColumnExpression) = OrExpression(this, other)