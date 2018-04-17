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

package com.fondesa.manganow.thread

/**
 * Defines a lambda which returns a generic result.
 *
 * @param T the result's type.
 */
typealias ExecutionBlock<T> = suspend () -> T

/**
 * Defines a lambda which will be invoked with a previously returned result as parameter.
 *
 * @param T the parameter's type.
 */
typealias CompletedBlock<T> = (T) -> Unit

/**
 * Defines a lambda which will be invoked with a [Throwable] as parameter.
 */
typealias ErrorBlock = (Throwable) -> Unit