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

import com.fondesa.database.annotations.AddToGraph
import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.*
import java.io.File
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic

/**
 *
 */
@Suppress("unused")
@AutoService(Processor::class)
class DatabaseProcessor : AbstractProcessor() {

    private val messenger get() = processingEnv.messager
    private val typeUtil get() = processingEnv.typeUtils
    private val elementUtil get() = processingEnv.elementUtils
    private val options get() = processingEnv.options

    private lateinit var kaptGeneratedDir: File

    override fun getSupportedAnnotationTypes() = setOf(AddToGraph::class.java.name)

    override fun getSupportedSourceVersion(): SourceVersion = SourceVersion.latest()

    override fun process(
        typeElements: MutableSet<out TypeElement>,
        env: RoundEnvironment
    ): Boolean {
        if (env.errorRaised() || env.processingOver()) {
            // Avoid the processing.
            return false
        }

        // Get the source directory.
        val kaptGeneratedDirPath = options["kapt.kotlin.generated"]?.replace("kaptKotlin", "kapt")

        if (kaptGeneratedDirPath == null) {
            printError("Can't find the target directory for generated Kotlin files.")
            return false
        }

        kaptGeneratedDir = File(kaptGeneratedDirPath)
        val parentFile = kaptGeneratedDir.parentFile
        if (!parentFile.exists()) {
            parentFile.mkdirs()
        }

        val tableElement = elementUtil.getTypeElement(DB_TABLE_CLASS.canonicalName)
        // Get all elements which must be added to the graph.
        val addToGraphElements = env.getElementsAnnotatedWith(AddToGraph::class.java)
        // Filter for valid elements.
        val validTableElements = addToGraphElements.filter { elem ->
            val valid = typeUtil.isAssignable(elem.asType(), tableElement.asType())
            if (!valid) {
                printError(
                    "You have to use a ${tableElement.qualifiedName}" +
                            " for the annotation ${AddToGraph::class.java.name}", elem
                )
                // The annotations are processed by this processor but an error is thrown.
                return true
            }
            valid
        }
        // Generate the graph with the processed elements.
        generateGraph(validTableElements)
        // The annotations are processed by this processor.
        return true
    }

    private fun generateGraph(tableElements: List<Element>) {
        val graphElement = elementUtil.getTypeElement(DB_GRAPH_CLASS.canonicalName)

        // Get the declaration of the function in the interface.
        val getTablesInterfaceFun = graphElement.enclosedElements
            .filterIsInstance<ExecutableElement>()
            .first { it.simpleName.toString() == "getTables" }

        val contentBuilder = TypeSpec.classBuilder(GENERATED_GRAPH_CLASS)
            // It will implement the interface and its methods.
            .addSuperinterface(graphElement.asType().asTypeName())

        val constructor = FunSpec.constructorBuilder()
            .addAnnotation(JAVAX_INJECT_CLASS)
            .build()

        contentBuilder.primaryConstructor(constructor)

        // Generate all the placeholders.
        val placeholder = tableElements.joinToString { "%T()" }
        // Generate the body of the method.
        val getTablesBody = CodeBlock.builder()
            .addStatement("return arrayOf($placeholder)", *tableElements.toTypedArray())
            .build()

        val getTablesFunction = FunSpec.overriding(getTablesInterfaceFun)
            .addCode(getTablesBody)
            .build()

        contentBuilder.addFunction(getTablesFunction)

        val file = FileSpec.builder(
            GENERATED_GRAPH_CLASS.packageName(),
            GENERATED_GRAPH_CLASS.simpleName()
        ).addType(contentBuilder.build())
            .build()

        file.writeTo(kaptGeneratedDir)
    }

    private fun printError(msg: CharSequence, elem: Element? = null) {
        messenger.printMessage(Diagnostic.Kind.ERROR, msg, elem)
    }

    companion object {
        private val JAVAX_INJECT_CLASS = ClassName("javax.inject", "Inject")

        private val DB_TABLE_CLASS = ClassName("com.fondesa.database.structure", "Table")
        private val DB_GRAPH_CLASS = ClassName("com.fondesa.database.structure", "Graph")

        private val GENERATED_GRAPH_CLASS = ClassName("com.fondesa.data.database", "AppGraph")
    }
}