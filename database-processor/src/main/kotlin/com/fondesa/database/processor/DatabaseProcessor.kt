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

import com.fondesa.database.annotations.Table
import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.TypeSpec
import java.io.File
import java.io.FileNotFoundException
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement

@AutoService(Processor::class)
class DatabaseProcessor : AbstractProcessor() {

    private val kaptGeneratedDir by lazy {
        // Get the source directory.
        val kaptGeneratedDirPath =
            processingEnv.options["kapt.kotlin.generated"]?.replace("kaptKotlin", "kapt")
                    ?: throw FileNotFoundException("Can't find the target directory for generated Kotlin files.")

        val dir = File(kaptGeneratedDirPath)
        val parentFile = dir.parentFile
        if (!parentFile.exists()) {
            parentFile.mkdirs()
        }
        dir
    }

    override fun getSupportedAnnotationTypes() = supportedAnnotationKClasses().map {
        it.java.name
    }.toMutableSet()

    override fun getSupportedSourceVersion(): SourceVersion = SourceVersion.latest()

    override fun process(
        typeElements: MutableSet<out TypeElement>,
        env: RoundEnvironment
    ): Boolean {

        val tableElements = env.getElementsAnnotatedWith(Table::class.java)
        tableElements.forEach {
            val className = it.simpleName.toString()
            val packageName = processingEnv.elementUtils.getPackageOf(it).toString()
            generateTable(packageName, className)
        }
        return true
    }

    private fun generateTable(packageName: String, className: String) {
        val generatedName = "Lyra$className"
        val tableClass = ClassName(packageName, generatedName)
        val classContent = TypeSpec.classBuilder(tableClass)
            .build()
        val file = FileSpec.builder(packageName, generatedName)
            .addType(classContent)
            .build()

        file.writeTo(kaptGeneratedDir)
    }

    private fun supportedAnnotationKClasses() = arrayOf(Table::class)
}