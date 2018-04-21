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

package com.fondesa.database.extension

import android.database.sqlite.SQLiteProgram

/**
 * Given an array of String [args], this method binds all of them in one single call.
 *
 * @param args the String array of bind args, none of which must be null.
 * @param gap the gap used to bind the arguments from a specific index one-based.
 */
fun SQLiteProgram.bindAllArgsAsStrings(args: Array<out String>, gap: Int) {
    args.forEachIndexed { index, arg ->
        bindString(gap + index + 1, arg)
    }
}