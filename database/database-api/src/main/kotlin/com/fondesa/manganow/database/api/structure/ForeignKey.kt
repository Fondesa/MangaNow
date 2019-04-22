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

package com.fondesa.manganow.database.api.structure

/**
 * Used to define a foreign key in a SQLite from.
 * The [ForeignKey] is built with [ForeignKeyConfig].
 *
 * @param fromColumns columns' name of the child table.
 * @param toColumns columns' name of the parent table referenced by the child table.
 * @param conflictStrategies list of strategies used when a conflict is found.
 */
class ForeignKey private constructor(
    internal val fromColumns: Array<out String>,
    internal val toTable: String,
    internal val toColumns: Array<out String>,
    internal val conflictStrategies: Array<ConflictStrategy>
) {

    companion object {

        fun fromConfig(tableName: String, config: ForeignKeyConfig): ForeignKey {
            val fromColumns = config.fromColumns
            val toColumns = config.toColumns
                ?: throw IllegalArgumentException("You have to specify the destination columns with to() method.")

            if (fromColumns.size != toColumns.size)
                throw IllegalArgumentException("The foreign key columns must be mapped 1 to 1.")

            return ForeignKey(
                fromColumns,
                tableName,
                toColumns,
                config.strategies.toTypedArray()
            )
        }
    }

    /**
     * Define the strategy to use when a conflict is found.
     *
     * @param clause update or delete clause.
     * @param action SQLite action invoked when a record in the parent table is updated/deleted.
     */
    internal data class ConflictStrategy(
        val clause: ForeignKeyClause,
        val action: ForeignKeyAction
    )
}