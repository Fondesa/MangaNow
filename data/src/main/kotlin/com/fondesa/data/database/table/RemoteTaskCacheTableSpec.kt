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

import com.fondesa.database.annotations.ColumnDefinition
import com.fondesa.database.annotations.ForeignKeyDefinition
import com.fondesa.database.annotations.TableDefinition
import com.fondesa.database.structure.ForeignKey
import com.fondesa.database.structure.IntegerColumnSpec
import com.fondesa.database.structure.RealColumnSpec
import com.fondesa.database.structure.TextColumnSpec

@TableDefinition("remote_task_cache")
object RemoteTaskCacheTableSpec {

    @ColumnDefinition("id")
    val COL_ID = IntegerColumnSpec.primaryKey()

    @ColumnDefinition("date_ms")
    val COL_DATE_MS = RealColumnSpec.notNull()

    @ColumnDefinition("task_path")
    val COL_PATH = TextColumnSpec.unique()

    @ForeignKeyDefinition(SortOrderTableSpec::class)
    val FROM_ID_TO_NAME = ForeignKey.spec()
        .from("id")
        .to("ciao")

}
