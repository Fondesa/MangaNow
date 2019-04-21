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

object Log: Logger {

    private lateinit var logger: Logger

    fun initialize(logger: Logger) {
        this.logger = logger
    }

    override fun d(message: String) {
        logger.d(message)
    }

    override fun i(message: String) {
        logger.i(message)
    }

    override fun w(message: String) {
        logger.w(message)
    }

    override fun e(message: String) {
        logger.e(message)
    }

    override fun e(t: Throwable) {
        logger.e(t)
    }

}