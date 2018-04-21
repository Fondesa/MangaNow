///*
// * Copyright (c) 2018 Fondesa
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *     http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package com.fondesa.database.structure
//
//import com.fondesa.database.extension.isNullOrEmpty
//
///**
// * Used to build a [ForeignKey] and specify its properties.
// */
//class Builder {
//    private var fromColumns: Array<out Column<*>>? = null
//    private var toColumns: Array<out Column<*>>? = null
//    private val strategies = mutableListOf<ForeignKey.ConflictStrategy>()
//
//    /**
//     * Defines the columns of the child table used in this foreign key.
//     *
//     * @param columns columns' name of the child table.
//     */
//    fun from(vararg columns: Column<*>) = apply { fromColumns = columns }
//
//    /**
//     * Defines the parent table and the columns of the table referenced by the child table.
//     *
//     * @param columns columns' name of the parent table referenced by the child table.
//     */
//    fun to(vararg columns: Column<*>) = apply {
//        toColumns = columns
//    }
//
//    /**
//     * Defines the action that must take place when the ON UPDATE clause must be propagated.
//     *
//     * @param action SQLite action invoked when a record in the parent table is updated.
//     */
//    fun onUpdate(action: ForeignKey.Action) = apply {
//        addStrategy(ForeignKey.Clause.UPDATE, action)
//    }
//
//    /**
//     * Defines the action that must take place when the ON DELETE clause must be propagated.
//     *
//     * @param action SQLite action invoked when a record in the parent table is deleted.
//     */
//    fun onDelete(action: ForeignKey.Action) = apply {
//        addStrategy(ForeignKey.Clause.DELETE, action)
//    }
//
//    private fun addStrategy(clause: ForeignKey.Clause, action: ForeignKey.Action) {
//        val previousStrategy = strategies.find { it.clause == clause }
//        if (previousStrategy != null) {
//            strategies.remove(previousStrategy)
//        }
//        strategies.add(ForeignKey.ConflictStrategy(clause, action))
//    }
//
//    /**
//     * Build a [ForeignKey] with its properties.
//     *
//     * @return build [ForeignKey].
//     */
//    fun build(): ForeignKey {
//        if (fromColumns.isNullOrEmpty())
//            throw IllegalArgumentException("You have to specify the start columns with from() method.")
//
//        if (toColumns.isNullOrEmpty())
//            throw IllegalArgumentException("You have to specify the destination columns with to() method.")
//
//        if (fromColumns!!.size != toColumns!!.size)
//            throw IllegalArgumentException("The foreign key columns must be mapped 1 to 1.")
//
//        columnsOfSameTable(fromColumns!!)
//        columnsOfSameTable(toColumns!!)
//
//        return ForeignKey(fromColumns!!, toColumns!!, strategies.toTypedArray())
//    }
//
//    private fun columnsOfSameTable(columns: Array<out Column<*>>) {
//        val table = columns.first().tableName
//        columns.forEach {
//            if (it.tableName != table)
//                throw IllegalArgumentException("The columns of the same foreign key must have the same table")
//        }
//    }
//}