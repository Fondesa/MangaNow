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

package com.fondesa.manganow.latest.impl

import androidx.annotation.StringRes
import com.fondesa.manganow.remote.api.exception.ConnectSocketException
import com.fondesa.manganow.remote.api.exception.ConnectivityException
import com.fondesa.manganow.remote.api.exception.TimeoutException

// TODO find a way to unify it
enum class ErrorCause {
    CONNECTIVITY, NETWORK, GENERIC
}

fun Throwable.toErrorCause(): ErrorCause = when (this) {
    is ConnectivityException -> ErrorCause.CONNECTIVITY
    is TimeoutException, is ConnectSocketException -> ErrorCause.NETWORK
    else -> ErrorCause.GENERIC
}

@StringRes
fun ErrorCause.toErrorMessage(): Int = when (this) {
    ErrorCause.CONNECTIVITY -> R.string.msg_connectivity_not_available
    ErrorCause.NETWORK -> R.string.msg_network_error
    ErrorCause.GENERIC -> R.string.msg_generic_error
}