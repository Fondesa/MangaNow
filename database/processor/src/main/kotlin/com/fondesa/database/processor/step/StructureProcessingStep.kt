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
import com.fondesa.database.processor.extension.*
import com.google.common.base.CaseFormat
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

    private val graphElement by lazy { elementUtil.getTypeElement(GRAPH_CLASS.canonicalName) }

    private val tableElement by lazy { elementUtil.getTypeElement(TABLE_CLASS.canonicalName) }

    private val columnSpecElement by lazy { elementUtil.getTypeElement(COLUMN_CONFIG_CLASS.canonicalName) }
    private val erasedColumnSpecType by lazy { typeUtil.erasure(columnSpecElement.asType()) }

    private val columnElement by lazy { elementUtil.getTypeElement(COLUMN_CLASS.canonicalName) }
    private val erasedColumnType by lazy { typeUtil.erasure(columnElement.asType()) }

    override fun filter() = arrayOf(Table::class)

    override fun process(elementsByAnnotation: SetMultimap<KClass<out Annotation>, Element>) {
        // Get all elements which must be added to the graph.
        val tableElements = elementsByAnnotation[Table::class]
        val tableClassNames = tableElements.map {
            generateTable(it)
        }
        // Generate the graph with the processed elements.
        generateGraph(tableClassNames)
    }

    private fun generateGraph(tableClassNames: List<ClassName>) {
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

    private fun generateTable(tableElement: Element): ClassName {
        val tableAnnotation = tableElement.getAnnotation(Table::class.java)
        val tableName = tableAnnotation.name
        val tableWithRowId = tableAnnotation.withRowId

        val packageName = elementUtil.getPackageOf(tableElement).toString()
        val generatedTableName = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, tableName)
        val className = ClassName(packageName, "${generatedTableName}Table")

        val contentBuilder = TypeSpec.classBuilder(className)
            // It will implement the interface and its methods.
            .addSuperinterface(this.tableElement.asType().asTypeName())

        val tableNameProperty = PropertySpec.builder("NAME", String::class)
            .initializer("%S", tableName)
            .build()

        val tableNamePropertyName = tableNameProperty.name

        val companionObjectBuilder = TypeSpec.companionObjectBuilder()
            .addProperty(tableNameProperty)

        val getNameFunction = FunSpec.overriding(this.tableElement.functionWithName("getName"))
            .returns(String::class)
            .addCode("return %L", tableNamePropertyName)
            .build()

        contentBuilder.addFunction(getNameFunction)

        val withRowIdFunction = FunSpec.overriding(this.tableElement.functionWithName("withRowId"))
            .returns(Boolean::class)
            .addCode("return %L", tableWithRowId)
            .build()

        contentBuilder.addFunction(withRowIdFunction)

        val columnsArray = tableElement.enclosedElements.map {
            it to it.getAnnotation(Column::class.java)
        }.filter { (_, definition) ->
            definition != null
        }.joinToString { (columnElement, definition) ->
            val columnProperty = generateColumnProperty(
                tableElement,
                tableNamePropertyName,
                columnElement,
                definition!!
            )
            // Add the property to the companion object.
            companionObjectBuilder.addProperty(columnProperty)
            // Return the name of the new property.
            columnProperty.name
        }

        // Generate the body of the function.
        val getColumnsBodyBuilder = CodeBlock.builder()
            .addStatement("return arrayOf(%N)", columnsArray)

        contentBuilder.companionObject(companionObjectBuilder.build())

        // Get the declaration of the function in the interface.
        val getColumnsFunction =
            FunSpec.overriding(this.tableElement.functionWithName("getColumns"))
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

    private fun generateColumnProperty(
        tableElement: Element,
        tableNamePropertyName: String,
        columnElement: Element,
        definition: Column
    ): PropertySpec {
        val elementType = columnElement.asType()
        if (!typeUtil.isAssignable(typeUtil.erasure(elementType), erasedColumnSpecType)) {
            throw ProcessingException(
                "It must be a ${columnSpecElement.qualifiedName}",
                columnElement
            )
        }
        val definitionName = definition.name
        val columnName = columnElement.simpleName.toString()
        val elementArgs = elementType.genericArguments()
        val argument = if (elementArgs.isEmpty()) {
            typeUtil.directSupertypes(elementType).first().genericArgument()
        } else {
            elementArgs.first()
        }
        val argumentType = argument.asTypeName().convertToKotlinType()

        val columnTypename = this.columnElement.asClassName()
            .asParameterizedTypeName(argumentType)

        return PropertySpec.builder(columnName, columnTypename)
            .initializer(
                "%T.fromConfig(%L, %S, %T.%N)",
                erasedColumnType,
                tableNamePropertyName,
                definitionName,
                tableElement.asType(),
                columnName
            )
            .build()
    }

    companion object {
        private val JAVAX_INJECT_CLASS = ClassName("javax.inject", "Inject")

        private val COLUMN_CONFIG_CLASS = ClassName("com.fondesa.database.structure", "ColumnConfig")
        private val COLUMN_CLASS = ClassName("com.fondesa.database.structure", "Column")
        private val TABLE_CLASS = ClassName("com.fondesa.database.structure", "Table")
        private val GRAPH_CLASS = ClassName("com.fondesa.database.structure", "Graph")

        private val GENERATED_GRAPH_CLASS = ClassName("com.fondesa.data.database", "AppGraph")
    }
}