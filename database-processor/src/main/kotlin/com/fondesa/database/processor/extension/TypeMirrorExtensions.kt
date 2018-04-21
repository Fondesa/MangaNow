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

package com.fondesa.database.processor.extension

import com.squareup.kotlinpoet.asTypeName
import javax.lang.model.type.DeclaredType
import javax.lang.model.type.TypeKind
import javax.lang.model.type.TypeMirror

fun TypeMirror.genericArguments(): List<TypeMirror> = if (kind === TypeKind.DECLARED) {
    (this as DeclaredType).typeArguments
} else throw TypeCastException("The type ${asTypeName()} isn't a declared type")

fun TypeMirror.genericArgument(): TypeMirror =
    genericArguments().firstOrNull()
            ?: throw ArrayIndexOutOfBoundsException("The type ${asTypeName()} hasn't type arguments")