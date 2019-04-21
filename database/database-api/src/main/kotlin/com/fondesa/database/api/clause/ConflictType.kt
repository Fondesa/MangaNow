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

package com.fondesa.database.api.clause

/**
 * Define the conflict type of SQLite.
 */
enum class ConflictType(val value: String) {

    /**
     * When a constraint violation occurs, an immediate ROLLBACK occurs, and the current transaction ends.
     * If no transaction is active (other than the implied transaction that is created on every command)
     * then this works the same as ABORT.
     */
    ROLLBACK("ROLLBACK"),

    /**
     * When a constraint violation occurs, the command backs out any prior changes it might have made and aborts.
     * But no ROLLBACK is executed so changes from prior commands within the same transaction are preserved.
     * This is the default behavior for SQLite.
     */
    ABORT("ABORT"),

    /**
     * When a constraint violation occurs, the command aborts.
     * But any changes to the database that the command made prior to encountering the constraint violation
     * are preserved and are not backed out.
     * For example, if an UPDATE statement encountered a constraint violation on the 100th row
     * that it attempts to update, then the first 99 row changes are preserved but change to rows 100 and beyond never occur.
     */
    FAIL("FAIL"),

    /**
     * When a constraint violation occurs, the one row that contains the constraint violation is not inserted or changed.
     * But the command continues executing normally.
     * Other rows before and after the row that contained the constraint violation continue to be inserted or updated normally.
     * No error is returned.
     */
    REPLACE("REPLACE"),

    /**
     * When a UNIQUE constraint violation occurs, the pre-existing row that caused the constraint violation is removed
     * prior to inserting or updating the current row.
     * Thus the insert or update always occurs.
     * The command continues executing normally.
     * No error is returned.
     */
    IGNORE("IGNORE")
}