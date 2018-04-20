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

package com.fondesa.database.processor.step

import com.fondesa.database.processor.extension.printError
import com.google.auto.common.BasicAnnotationProcessor
import com.google.common.collect.ImmutableSetMultimap
import com.google.common.collect.SetMultimap
import com.squareup.kotlinpoet.FileSpec
import java.io.File
import javax.annotation.processing.Messager
import javax.lang.model.element.Element
import javax.lang.model.util.Elements
import javax.lang.model.util.Types
import kotlin.reflect.KClass

abstract class BasicProcessingStep(
    private val kaptGeneratedDir: File,
    protected val elementUtil: Elements,
    protected val typeUtil: Types,
    protected val messenger: Messager
) : BasicAnnotationProcessor.ProcessingStep {

    final override fun process(elementsByAnnotation: SetMultimap<Class<out Annotation>, Element>): Set<Element> {
        val kClassMapBuilder = ImmutableSetMultimap.builder<KClass<out Annotation>, Element>()
        elementsByAnnotation.forEach { annotation, elem ->
            kClassMapBuilder.put(annotation.kotlin, elem)
        }
        val kClassMap = kClassMapBuilder.build()

        try {
            process(kClassMap)
        } catch (e: ProcessingException) {
            messenger.printError(e.msg, e.element)
        }
        return emptySet()
    }

    final override fun annotations(): Set<Class<out Annotation>> = filter().map {
        it.java
    }.toSet()

    protected abstract fun filter(): Array<out KClass<out Annotation>>

    protected abstract fun process(elementsByAnnotation: SetMultimap<KClass<out Annotation>, Element>)

    protected fun FileSpec.writeToKaptGeneratedDir() = writeTo(kaptGeneratedDir)
}