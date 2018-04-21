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

import com.fondesa.database.annotations.*

//class RemoteTaskCacheTable : com.fondesa.database.structure.Table {
//
//    override fun getName() = NAME
//
//    override fun getColumns(): Array<Column> = arrayOf(COL_ID, COL_PATH, COL_DATE_MS)
//
//    companion object {
//        const val NAME = "remote_task_cache"
//
//        val COL_ID = IntegerColumnBuilder(NAME, "id").primaryKey().build()
//        val COL_DATE_MS = IntegerColumnBuilder(NAME, "date_ms").notNull().build()
//        val COL_PATH = TextColumnBuilder(NAME, "task_path").unique().build()
//    }
//}


@TableDefinition("example_table", withRowId = true)
object ExampleTableSpec {

    @ColumnDefinition("id")
    val COL_ID = IntegerColumnSpec

    @ColumnDefinition("ciao")
    val COL_CIAO = RealColumnSpec.primaryKey()
}

@TableDefinition("ciao_table", withRowId = false)
object CiaoTableSpec {

    @ColumnDefinition("saf")
    val COL_SAF = BlobColumnSpec.primaryKey()

    @ColumnDefinition("jhj")
    val COL_KOT = TextColumnSpec.primaryKey()
}