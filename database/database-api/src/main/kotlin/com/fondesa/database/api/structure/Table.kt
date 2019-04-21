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

/**
 * Interface used to define the properties of a SQLite table.
 */
interface Table {

    /**
     * @return name of the table used in SQLite statements.
     */
    fun getName(): String

    /**
     * Defines the properties of the columns of this table.
     *
     * @return all columns of this table excluding *rowId used internally by SQLite.
     */
    fun getColumns(): Array<Column<*>>

    /**
     * Defines the foreign keys of this table to relate it to other tables.
     *
     * @return all foreign keys that starts from this table.
     */
    fun getForeignKeys(): Array<ForeignKey>

    /**
     * Specify if this table must use the inner autoincrement *rowId column of SQLite.
     * This method could be used as optimizer when a lot of data must be inserted or queried.
     * For example, it could be useful in a many-to-many relationship to avoid an autoincrement id
     * when the primary key is defined as a composite primary key of two ids.
     * This method will take effects only on api >= 21.
     *
     * @return true if the table must use the inner autoincrement *rowId column
     */
    fun withRowId(): Boolean
}