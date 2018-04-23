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

package com.fondesa.data.remote.database

import com.fondesa.database.annotations.Column
import com.fondesa.database.annotations.Table
import com.fondesa.database.structure.IntegerColumnConfig
import com.fondesa.database.structure.RealColumnConfig
import com.fondesa.database.structure.TextColumnConfig

@Table("remote_task")
object RemoteTaskTableConfig {

    @Column("id")
    val COL_ID = IntegerColumnConfig().primaryKey()

    @Column("key")
    val COL_KEY = TextColumnConfig().unique()

    @Column("date_ms")
    val COL_DATE_MS = RealColumnConfig().notNull()
}
