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

enum class ForeignKeyAction(val value: String) {

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