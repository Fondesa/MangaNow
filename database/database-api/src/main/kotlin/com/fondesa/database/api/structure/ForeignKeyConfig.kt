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

package com.fondesa.database.api.structure

import com.fondesa.database.api.extension.plusArray

/**
 * Used to build a [ForeignKey] and specify its properties.
 */
class ForeignKeyConfig private constructor(internal val fromColumns: Array<out String>) {
    internal var toColumns: Array<out String>? = null
    internal val strategies = mutableListOf<ForeignKey.ConflictStrategy>()

    /**
     * Defines the parent table and the columns of the table referenced by the child table.
     * The columns' arguments are split because at least one column is required.
     *
     * @param column first column' name of the parent table referenced by the child table.
     * @param others other columns' name of the parent table referenced by the child table.
     */
    fun to(column: String, vararg others: String) = apply {
        toColumns = column.plusArray(others)
    }

    /**
     * Defines the action that must take place when the ON UPDATE clause must be propagated.
     *
     * @param action SQLite action invoked when a record in the parent table is updated.
     */
    fun onUpdate(action: ForeignKeyAction) = apply {
        addStrategy(ForeignKeyClause.UPDATE, action)
    }

    /**
     * Defines the action that must take place when the ON DELETE clause must be propagated.
     *
     * @param action SQLite action invoked when a record in the parent table is deleted.
     */
    fun onDelete(action: ForeignKeyAction) = apply {
        addStrategy(ForeignKeyClause.DELETE, action)
    }

    private fun addStrategy(clause: ForeignKeyClause, action: ForeignKeyAction) {
        val previousStrategy = strategies.find { it.clause == clause }
        if (previousStrategy != null) {
            strategies.remove(previousStrategy)
        }
        strategies.add(ForeignKey.ConflictStrategy(clause, action))
    }

    companion object {

        /**
         * Defines the columns of the child table used in this foreign key.
         *
         * @param columns columns' name of the child table.
         */
        fun from(column: String, vararg others: String) =
            ForeignKeyConfig(column.plusArray(others))
    }
}
