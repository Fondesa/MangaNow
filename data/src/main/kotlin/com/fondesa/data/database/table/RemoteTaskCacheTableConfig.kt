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

import com.fondesa.database.annotations.Column
import com.fondesa.database.annotations.ForeignKey
import com.fondesa.database.annotations.Table
import com.fondesa.database.structure.ForeignKeyConfig
import com.fondesa.database.structure.RealColumnConfig
import com.fondesa.database.structure.TextColumnConfig

@Table("remote_task_cache")
object RemoteTaskCacheTableConfig {

    @Column("id")
    val COL_ID = RealColumnConfig

    @Column("date_ms")
    val COL_DATE_MS = RealColumnConfig.notNull()

    @Column("task_path")
    val COL_PATH = TextColumnConfig.unique()

    @ForeignKey(SortOrderTableConfig::class)
    val FROM_ID_TO_NAME = ForeignKeyConfig.from("id").to("ciao")

}
