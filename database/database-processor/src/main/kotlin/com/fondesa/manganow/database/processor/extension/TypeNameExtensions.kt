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

package com.fondesa.manganow.database.processor.extension

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.ParameterizedTypeName
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.asTypeName
import kotlin.reflect.KClass
import kotlin.reflect.jvm.internal.impl.name.FqName
import kotlin.reflect.jvm.internal.impl.platform.JavaToKotlinClassMap

fun TypeName.convertToKotlinType(): TypeName = if (this is ParameterizedTypeName) {
    val rawTypeName = rawType.convertToKotlinType() as ClassName
    val typeArguments = typeArguments.map { it.convertToKotlinType() }.toTypedArray()

    if (rawTypeName.simpleName() == "Array" && typeArguments.size == 1) {
        typeArguments.first().toMappedPrimitiveArrayType()
                ?: rawTypeName.asParameterizedTypeName(*typeArguments)
    } else {
        rawTypeName.asParameterizedTypeName(*typeArguments)
    }
} else {
    JavaToKotlinClassMap.INSTANCE.mapJavaToKotlin(FqName(toString()))
        ?.asSingleFqName()
        ?.asString()
        ?.let {
            ClassName.bestGuess(it)
        } ?: this
}

private fun TypeName.toMappedPrimitiveArrayType(): TypeName? {
    val map = mutableMapOf<KClass<*>, KClass<*>>()
    map[Byte::class] = ByteArray::class
    map[Char::class] = CharArray::class
    map[Short::class] = ShortArray::class
    map[Int::class] = IntArray::class
    map[Long::class] = LongArray::class
    map[Float::class] = FloatArray::class
    map[Double::class] = DoubleArray::class
    map[Boolean::class] = BooleanArray::class

    val type = map.keys.firstOrNull {
        it.asTypeName() == this
    }

    return type?.let {
        val typeName = map[type]
        typeName?.asTypeName()
    }
}