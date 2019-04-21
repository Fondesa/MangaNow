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

package com.fondesa.log.api

/**
 * Used as a logging facade in the application.
 */
interface Logger {

    /**
     * Logs with a debug priority.
     *
     * @param message the message to log.
     */
    fun d(message: String)

    /**
     * Logs with an info priority.
     *
     * @param message the message to log.
     */
    fun i(message: String)

    /**
     * Logs with a warning priority.
     *
     * @param message the message to log.
     */
    fun w(message: String)

    /**
     * Logs with an error priority.
     *
     * @param message the message to log.
     */
    fun e(message: String)

    /**
     * Logs with an error priority.
     *
     * @param t the [Throwable] to log.
     */
    fun e(t: Throwable)
}