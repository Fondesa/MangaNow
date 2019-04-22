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

package com.fondesa.manganow.database.api.statement

import com.fondesa.manganow.database.api.execution.PlainExecutor
import com.fondesa.manganow.database.api.statement.base.SQLiteCompiledStatement

/**
 * Helper class used to create a VACUUM statement of SQLite.
 * The VACUUM statement is used to rebuild the database file, repacking it
 * into a minimal amount of disk space.
 */
object Vacuum {

    /**
     * Creates a raw VACUUM [Statement].
     *
     * @return [Statement] with a [PlainExecutor].
     */
    fun create() = SQLiteCompiledStatement.lined("VACUUM") {
        PlainExecutor(it) {
            it.execute()
        }
    }
}