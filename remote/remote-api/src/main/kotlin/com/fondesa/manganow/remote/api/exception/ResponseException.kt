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

package com.fondesa.manganow.remote.api.exception

/**
 * Exception thrown if an error occurs when the response (of a WS request) is received.
 *
 * @param url the url of the server.
 * @param code HTTP response code.
 * @param reason technical reason explaining why this exception is thrown.
 */
class ResponseException(url: String, val code: Int, val reason: String) :
    Exception("Unsuccessful response from \"$url\" with code: '$code' and reason: '$reason'")