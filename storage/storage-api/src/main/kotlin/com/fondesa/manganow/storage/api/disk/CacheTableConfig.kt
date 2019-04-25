/*
 * Copyright (c) 2019 Fondesa
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

package com.fondesa.manganow.storage.api.disk

import com.fondesa.manganow.database.annotations.Column
import com.fondesa.manganow.database.annotations.Table
import com.fondesa.manganow.database.api.structure.IntegerColumnConfig
import com.fondesa.manganow.database.api.structure.RealColumnConfig
import com.fondesa.manganow.database.api.structure.TextColumnConfig

@Table("cache")
object CacheTableConfig {

    @Column("id")
    val COL_ID = IntegerColumnConfig().primaryKey()

    @Column("key")
    val COL_KEY = TextColumnConfig().unique()

    @Column("date_ms")
    val COL_DATE_MS = RealColumnConfig().notNull()
}
