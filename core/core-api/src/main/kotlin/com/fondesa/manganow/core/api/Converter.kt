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

package com.fondesa.manganow.core.api

/**
 * Used to convert a value of type [FromType] to a value of type [ToType].
 *
 * @param FromType type of the value that must be converted.
 * @param ToType type of the value after the conversion.
 */
interface Converter<in FromType, out ToType> {

    /**
     * Convert the value [value] to a value of type [ToType].
     *
     * @param value value that must be converted.
     * @return value after the conversion.
     */
    fun convert(value: FromType): ToType
}