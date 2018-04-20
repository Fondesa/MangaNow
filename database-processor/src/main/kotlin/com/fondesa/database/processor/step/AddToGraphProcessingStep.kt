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

import com.fondesa.database.annotations.AddToGraph
import com.google.common.collect.SetMultimap
import com.squareup.kotlinpoet.*
import java.io.File
import javax.annotation.processing.Messager
import javax.lang.model.element.Element
import javax.lang.model.element.ExecutableElement
import javax.lang.model.util.Elements
import javax.lang.model.util.Types
import kotlin.reflect.KClass

class AddToGraphProcessingStep(
    kaptGeneratedDir: File,
    elementUtil: Elements,
    typeUtil: Types,
    messenger: Messager
) : BasicProcessingStep(kaptGeneratedDir, elementUtil, typeUtil, messenger) {

    override fun filter() = arrayOf(AddToGraph::class)

    override fun process(elementsByAnnotation: SetMultimap<KClass<out Annotation>, Element>) {
        val tableElement = elementUtil.getTypeElement(DB_TABLE_CLASS.canonicalName)
        // Get all elements which must be added to the graph.
        val addToGraphElements = elementsByAnnotation[AddToGraph::class]
        // Filter for valid elements.
        val validTableElements = addToGraphElements.filter { elem ->
            val valid = typeUtil.isAssignable(elem.asType(), tableElement.asType())
            if (!valid) {
                // The annotations are processed by this processor but an error is thrown.
                throw ProcessingException(
                    "You have to use a ${tableElement.qualifiedName}" +
                            " for the annotation ${AddToGraph::class.java.name}", elem
                )
            }
            valid
        }
        // Generate the graph with the processed elements.
        generateGraph(validTableElements)
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

        file.writeToKaptGeneratedDir()
    }

    companion object {
        private val JAVAX_INJECT_CLASS = ClassName("javax.inject", "Inject")

        private val DB_TABLE_CLASS = ClassName("com.fondesa.database.structure", "Table")
        private val DB_GRAPH_CLASS = ClassName("com.fondesa.database.structure", "Graph")

        private val GENERATED_GRAPH_CLASS = ClassName("com.fondesa.data.database", "AppGraph")
    }
}