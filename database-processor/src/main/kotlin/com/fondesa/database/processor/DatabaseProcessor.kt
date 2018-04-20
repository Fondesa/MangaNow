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

package com.fondesa.database.processor

import com.fondesa.database.processor.extension.printError
import com.fondesa.database.processor.step.AddToGraphProcessingStep
import com.google.auto.common.BasicAnnotationProcessor
import com.google.auto.service.AutoService
import java.io.File
import javax.annotation.processing.Processor
import javax.lang.model.SourceVersion

@AutoService(Processor::class)
class DatabaseProcessor : BasicAnnotationProcessor() {

    private val messenger get() = processingEnv.messager
    private val typeUtil get() = processingEnv.typeUtils
    private val elementUtil get() = processingEnv.elementUtils
    private val options get() = processingEnv.options

    override fun getSupportedSourceVersion(): SourceVersion = SourceVersion.latest()

    override fun initSteps(): Iterable<ProcessingStep> {
        // Get the source directory.
        val kaptGeneratedDirPath = options["kapt.kotlin.generated"]?.replace("kaptKotlin", "kapt")

        if (kaptGeneratedDirPath == null) {
            messenger.printError("Can't find the target directory for generated Kotlin files.")
            return emptyList()
        }

        val kaptGeneratedDir = File(kaptGeneratedDirPath)
        val parentFile = kaptGeneratedDir.parentFile
        if (!parentFile.exists()) {
            parentFile.mkdirs()
        }

        return listOf(
            AddToGraphProcessingStep(
                kaptGeneratedDir,
                elementUtil,
                typeUtil,
                messenger
            )
        )
    }
}