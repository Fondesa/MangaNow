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

import com.fondesa.database.annotations.Column
import com.fondesa.database.annotations.Table
import com.fondesa.database.processor.extension.asParameterizedTypeName
import com.fondesa.database.processor.extension.convertToKotlinType
import com.fondesa.database.processor.extension.functionWithName
import com.fondesa.database.processor.extension.genericArgument
import com.google.common.collect.SetMultimap
import com.squareup.kotlinpoet.*
import java.io.File
import javax.annotation.processing.Messager
import javax.lang.model.element.Element
import javax.lang.model.util.Elements
import javax.lang.model.util.Types
import kotlin.reflect.KClass


class StructureProcessingStep(
    kaptGeneratedDir: File,
    elementUtil: Elements,
    typeUtil: Types,
    messenger: Messager
) : BasicProcessingStep(kaptGeneratedDir, elementUtil, typeUtil, messenger) {

    private val tableElement by lazy { elementUtil.getTypeElement(DB_TABLE_CLASS.canonicalName) }

    private val columnElement by lazy { elementUtil.getTypeElement(DB_COLUMN_CLASS.canonicalName) }
    private val erasedColumnType by lazy { typeUtil.erasure(columnElement.asType()) }

    override fun filter() = arrayOf(Table::class)

    override fun process(elementsByAnnotation: SetMultimap<KClass<out Annotation>, Element>) {
        // Get all elements which must be added to the graph.
        val addToGraphElements = elementsByAnnotation[Table::class]
        val tableClassNames = addToGraphElements.map {
            generateTable(it)
        }
        // Generate the graph with the processed elements.
        generateGraph(tableClassNames)
    }

    private fun generateTable(element: Element): ClassName {
        val tableAnnotation = element.getAnnotation(Table::class.java)
        val tableName = tableAnnotation.value
        val tableWithRowId = tableAnnotation.withRowId

        val simpleName = element.simpleName
        val packageName = elementUtil.getPackageOf(element).toString()
        val className = ClassName(packageName, "Lyra$simpleName")

        val contentBuilder = TypeSpec.classBuilder(className)
            // It will implement the interface and its methods.
            .addSuperinterface(tableElement.asType().asTypeName())

        val getNameFunction = FunSpec.overriding(tableElement.functionWithName("getName"))
            .returns(String::class)
            .addCode("return %S", tableName)
            .build()

        contentBuilder.addFunction(getNameFunction)

        val withRowIdFunction = FunSpec.overriding(tableElement.functionWithName("withRowId"))
            .returns(Boolean::class)
            .addCode("return %L", tableWithRowId)
            .build()

        contentBuilder.addFunction(withRowIdFunction)

        val companionObjectBuilder = TypeSpec.companionObjectBuilder()

        val columnsArray = element.enclosedElements.filter {
            it.getAnnotation(Column::class.java) != null
        }.joinToString {
            // Columns which must be included.
            val columnName = it.simpleName.toString()
            val argumentType = it.asType().genericArgument()
                .asTypeName()
                .convertToKotlinType()

            val columnTypename = columnElement.asClassName()
                .asParameterizedTypeName(argumentType)

            val property = PropertySpec.builder(columnName, columnTypename)
                .initializer(
                    "%T.fromSpec(%S, %T.%N)",
                    erasedColumnType,
                    tableName,
                    element.asType(),
                    columnName
                )
                .build()

            companionObjectBuilder.addProperty(property)
            columnName
        }

        // Generate the body of the function.
        val getColumnsBodyBuilder = CodeBlock.builder()
            .addStatement("return arrayOf(%N)", columnsArray)

        contentBuilder.companionObject(companionObjectBuilder.build())

        // Get the declaration of the function in the interface.
        val getColumnsFunction = FunSpec.overriding(tableElement.functionWithName("getColumns"))
            .addCode(getColumnsBodyBuilder.build())
            .build()

        contentBuilder.addFunction(getColumnsFunction)

        val file = FileSpec.builder(
            className.packageName(),
            className.simpleName()
        ).addType(contentBuilder.build())
            .build()

        file.writeToKaptGeneratedDir()
        return className
    }

    private fun generateGraph(tableClassNames: List<ClassName>) {
        val graphElement = elementUtil.getTypeElement(DB_GRAPH_CLASS.canonicalName)

        val contentBuilder = TypeSpec.classBuilder(GENERATED_GRAPH_CLASS)
            // It will implement the interface and its methods.
            .addSuperinterface(graphElement.asType().asTypeName())

        val constructor = FunSpec.constructorBuilder()
            .addAnnotation(JAVAX_INJECT_CLASS)
            .build()

        contentBuilder.primaryConstructor(constructor)

        // Generate all the placeholders.
        val placeholder = tableClassNames.joinToString { "%T()" }
        // Generate the body of the  // Generate the body of the method.
        val getTablesBody = CodeBlock.builder()
            .addStatement("return arrayOf($placeholder)", *tableClassNames.toTypedArray())
            .build()

        val getTablesFunction = FunSpec.overriding(graphElement.functionWithName("getTables"))
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

        private val DB_COLUMN_CLASS = ClassName("com.fondesa.database.structure", "Column")
        private val DB_TABLE_CLASS = ClassName("com.fondesa.database.structure", "Table")
        private val DB_GRAPH_CLASS = ClassName("com.fondesa.database.structure", "Graph")

        private val GENERATED_GRAPH_CLASS = ClassName("com.fondesa.data.database", "AppGraph")
    }
}