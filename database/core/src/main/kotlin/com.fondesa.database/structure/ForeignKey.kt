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

package com.fondesa.database.structure

import com.fondesa.database.extension.isNullOrEmpty

/**
 * Used to define a foreign key in a SQLite from.
 * The [ForeignKey] is built with [ForeignKey.Definition].
 *
 * @param fromColumns columns' name of the child table.
 * @param toColumns columns' name of the parent table referenced by the child table.
 * @param conflictStrategies list of strategies used when a conflict is found.
 */
class ForeignKey private constructor(
    val fromColumns: Array<out String>,
    val toTable: String,
    val toColumns: Array<out String>,
    val conflictStrategies: Array<ForeignKey.ConflictStrategy>
) {

    companion object {

        fun spec() = Definition()

        fun fromDefinition(tableName:String,definition: Definition): ForeignKey {
            if (definition.fromColumns.isNullOrEmpty())
                throw IllegalArgumentException("You have to specify the start columns with from() method.")

            if (definition.toColumns.isNullOrEmpty())
                throw IllegalArgumentException("You have to specify the destination columns with to() method.")

            if (definition.fromColumns!!.size != definition.toColumns!!.size)
                throw IllegalArgumentException("The foreign key columns must be mapped 1 to 1.")

            return ForeignKey(definition.fromColumns!!,
                tableName,
                definition.toColumns!!,
                definition.strategies.toTypedArray())
        }
    }

    /**
     * Used to build a [ForeignKey] and specify its properties.
     */
    class Definition {
        internal var fromColumns: Array<out String>? = null
        internal var toColumns: Array<out String>? = null
        internal val strategies = mutableListOf<ConflictStrategy>()

        /**
         * Defines the columns of the child table used in this foreign key.
         *
         * @param columns columns' name of the child table.
         */
        fun from(vararg columnNames: String) = apply { fromColumns = columnNames }

        /**
         * Defines the parent table and the columns of the table referenced by the child table.
         *
         * @param columns columns' name of the parent table referenced by the child table.
         */
        fun to(vararg columnNames: String) = apply {
            toColumns = columnNames
        }

        /**
         * Defines the action that must take place when the ON UPDATE clause must be propagated.
         *
         * @param action SQLite action invoked when a record in the parent table is updated.
         */
        fun onUpdate(action: Action) = apply {
            addStrategy(Clause.UPDATE, action)
        }

        /**
         * Defines the action that must take place when the ON DELETE clause must be propagated.
         *
         * @param action SQLite action invoked when a record in the parent table is deleted.
         */
        fun onDelete(action: Action) = apply {
            addStrategy(Clause.DELETE, action)
        }

        private fun addStrategy(clause: Clause, action: Action) {
            val previousStrategy = strategies.find { it.clause == clause }
            if (previousStrategy != null) {
                strategies.remove(previousStrategy)
            }
            strategies.add(ConflictStrategy(clause, action))
        }
    }

    /**
     * Define the strategy to use when a conflict is found.
     *
     * @param clause update or delete clause.
     * @param action SQLite action invoked when a record in the parent table is updated/deleted.
     */
    data class ConflictStrategy(val clause: Clause, val action: Action)

    enum class Clause(val value: String) {

        /**
         * This clause is used to define an action that must be invoked on the child table
         * when a record of the parent table is deleted.
         */
        DELETE("on delete"),

        /**
         * This clause is used to define an action that must be invoked on the child table
         * when a record of the parent table is updated.
         */
        UPDATE("on update")
    }

    enum class Action(val value: String) {

        /**
         * This action means that when a parent key is modified or deleted from the database,
         * no special action is taken.
         */
        NONE("no action"),

        /**
         * This action means that the application is prohibited from deleting or modifying (depending on the clause)
         * a parent key when there exists one or more child keys mapped to it.
         * The difference between the effect of a restrict action and normal foreign key constraint enforcement is
         * that the restrict action processing happens as soon as the field is updated.
         */
        RESTRICT("restrict"),

        /**
         * This actions means that when a parent key is deleted or modified (depending on the clause),
         * the child key columns of all rows in the child table that mapped to the parent key are set to null values.
         */
        SET_NULL("set null"),

        /**
         * This actions means that when a parent key is deleted or modified (depending on the clause),
         * the child key columns of all rows in the child table that mapped to the parent key are set to default values.
         */
        SET_DEFAULT("set default"),

        /**
         * This actions means that each row in the child table that was associated with the deleted/updated
         * parent row is also deleted/updated.
         */
        CASCADE("cascade")
    }
}