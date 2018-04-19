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

package com.fondesa.data.database.table

import com.fondesa.database.structure.Column
import com.fondesa.database.structure.IntegerColumnBuilder
import com.fondesa.database.structure.Table
import com.fondesa.database.structure.TextColumnBuilder

@com.fondesa.database.annotations.Table
class RemoteTaskCacheTable : Table {

    override fun getName() = NAME

    override fun getColumns(): Array<Column<*>> = arrayOf(COL_ID, COL_PATH, COL_DATE_MS)

    companion object {
        const val NAME = "remote_task_cache"

        val COL_ID = IntegerColumnBuilder(NAME, "id").primaryKey().build()
        val COL_DATE_MS = IntegerColumnBuilder(NAME, "date_ms").notNull().build()
        val COL_PATH = TextColumnBuilder(NAME, "task_path").unique().build()
    }
}