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

package com.fondesa.database.api.extension

/**
 * Creates an array adding an array of the same type to the original element.
 *
 * @param array array of the same type of the original element that must be added.
 * @return an array containing the original element and the elements of the given [array].
 */
internal inline fun <reified T> T.plusArray(array: Array<out T>) = arrayOf(this).plus(array)