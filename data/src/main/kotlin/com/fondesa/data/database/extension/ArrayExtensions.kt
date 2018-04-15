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

package com.fondesa.data.database.extension

/**
 * Creates a string containing the elements of the array separated by a character.
 * E.g. with the given array ["red", "green", "blue"] and the separator "|",
 * the result will be: "red|green|blue"
 *
 * @param separator character used to separate the array's elements.
 * @param transform transformation applied to the array's element to map it to String.
 * @return string containing the elements of the array separated by the given [separator].
 */
fun <T> Array<T>.interpolateWith(separator: Char, transform: ((T) -> String) = { it.toString() }) =
    interpolateWith(separator.toString(), transform)

/**
 * Creates a string containing the elements of the array separated by another string.
 * E.g. with the given array ["red", "green", "blue"] and the separator "_$_",
 * the result will be: "red_$_green_$_blue"
 *
 * @param separator string used to separate the array's elements.
 * @param transform transformation applied to the array's element to map it to String.
 * @return string containing the elements of the array separated by the given [separator].
 */
fun <T> Array<T>.interpolateWith(
    separator: String,
    transform: ((T) -> String) = { it.toString() }
): String {
    val sb = StringBuilder()
    forEachIndexed { index, elem ->
        // Append the mapped element.
        sb.append(transform(elem))
        if (index < lastIndex) {
            // Append the separator if this element isn't the last one.
            sb.append(separator)
        }
    }
    return sb.toString()
}

/**
 * @return true if the array is null or empty.
 */
fun <T> Array<T>?.isNullOrEmpty(): Boolean = this?.isEmpty() ?: true