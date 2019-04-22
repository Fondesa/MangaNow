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

package com.fondesa.manganow.converter

import android.content.Context
import androidx.annotation.StringRes
import com.fondesa.data.converter.Converter
import com.fondesa.manganow.R
import com.fondesa.manganow.remote.api.exception.ConnectivityException
import com.fondesa.manganow.remote.api.exception.TimeoutException
import javax.inject.Inject

/**
 * Implementation of [Converter] used to convert an [Exception] to a message
 * that will be shown to the user.
 *
 * @param context [Context] used to access to resources.
 */
class ThrowableConverter @Inject constructor(private val context: Context) :
    Converter<Throwable, String> {

    override fun convert(value: Throwable): String {
        @StringRes val message = when (value) {

            is TimeoutException -> R.string.msg_timeout_occurred

            is ConnectivityException -> R.string.msg_connectivity_not_available

            else -> R.string.msg_generic_error
        }
        return context.getString(message)
    }
}