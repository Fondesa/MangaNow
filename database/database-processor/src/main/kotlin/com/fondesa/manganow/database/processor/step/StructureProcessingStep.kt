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

package com.fondesa.manganow.database.processor.step

import com.fondesa.manganow.database.api.structure.*
import com.fondesa.manganow.database.processor.extension.*
import com.google.common.base.CaseFormat
import com.google.common.collect.SetMultimap
import com.squareup.kotlinpoet.*
import java.io.File
import javax.annotation.processing.Messager
import javax.inject.Inject
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.lang.model.type.TypeMirror
import javax.lang.model.util.Elements
import javax.lang.model.util.Types
import kotlin.reflect.KClass
import com.fondesa.manganow.database.annotations.Column as ColumnAnnotation
import com.fondesa.manganow.database.annotations.ForeignKey as ForeignKeyAnnotation
import com.fondesa.manganow.database.annotations.Table as TableAnnotation

class StructureProcessingStep(
    kaptGeneratedDir: File,
    elementUtil: Elements,
    typeUtil: Types,
    messenger: Messager
) : BasicProcessingStep(kaptGeneratedDir, elementUtil, typeUtil, messenger) {

    private val graphElement by lazyTypeElement(Graph::class)

    private val tableElement by lazyTypeElement(Table::class)

    private val foreignKeyElement by lazyTypeElement(ForeignKey::class)
    private val foreignKeyConfigElement by lazyTypeElement(ForeignKeyConfig::class)

    private val columnConfigElement by lazyTypeElement(ColumnConfig::class)
    private val erasedColumnConfigType by lazyErasedType(columnConfigElement)

    private val columnElement by lazyTypeElement(Column::class)
    private val erasedColumnType by lazyErasedType(columnElement)

    override fun filter() = arrayOf(TableAnnotation::class)

    override fun process(elementsByAnnotation: SetMultimap<KClass<out Annotation>, Element>) {
        // Get all elements which must be added to the graph.
        val tableElements = elementsByAnnotation[TableAnnotation::class]
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
            .addAnnotation(Inject::class)
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
            Graph::class.asClassName().packageName(),
            GENERATED_GRAPH_CLASS.simpleName()
        ).addType(contentBuilder.build())
            .build()

        file.writeToKaptGeneratedDir()
    }

    private fun generateTable(tableElement: Element): ClassName {
        val tableAnnotation = tableElement.getAnnotation(TableAnnotation::class.java)
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
            it to it.getAnnotation(ColumnAnnotation::class.java)
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

        val annotatedWithForeignKeys = tableElement.enclosedElements.filter {
            it.getAnnotation(ForeignKeyAnnotation::class.java) != null
        }
        val getForeignKeysBodyBuilder = CodeBlock.builder()

        if (annotatedWithForeignKeys.isEmpty()) {
            getForeignKeysBodyBuilder.addStatement("return emptyArray()")
        } else {
            val foreignKeyType = foreignKeyElement.asType()

            getForeignKeysBodyBuilder.addStatement("val list = mutableListOf<%T>()", foreignKeyType)

            annotatedWithForeignKeys.forEach { fkElement ->
                val elementType = fkElement.asType()
                if (!typeUtil.isAssignable(elementType, foreignKeyConfigElement.asType())) {
                    throw ProcessingException(
                        "It must be a ${foreignKeyConfigElement.qualifiedName}",
                        fkElement
                    )
                }
                var destinationType: TypeMirror? = null
                fkElement.annotationMirrors.forEach {
                    it.elementValues.forEach {
                        if (it.key.simpleName.toString() == "destination") {
                            destinationType = it.value.value as TypeMirror
                        }
                    }
                }
                val destinationTableType = destinationType ?: throw ProcessingException(
                    "An error occurred retrieving the destination of this element",
                    fkElement
                )
                val destinationTableElement =
                    elementUtil.getTypeElement(destinationTableType.asTypeName().toString())
                val destinationTableAnnotation =
                    destinationTableElement.getAnnotation(TableAnnotation::class.java)
                        ?: throw ProcessingException(
                            "The class ${destinationTableElement.qualifiedName}" +
                                    " must be annotate with ${Table::class.java.name}",
                            destinationTableElement
                        )

                getForeignKeysBodyBuilder.addStatement(
                    "list.add(%T.fromConfig(%S, %T.%N))",
                    foreignKeyType,
                    destinationTableAnnotation.name,
                    tableElement.asType(),
                    fkElement.simpleName.toString()
                )
            }
            getForeignKeysBodyBuilder.addStatement("return list.toTypedArray()")
        }

        // Get the declaration of the function in the interface.
        val getForeignKeysFunction =
            FunSpec.overriding(this.tableElement.functionWithName("getForeignKeys"))
                .addCode(getForeignKeysBodyBuilder.build())
                .build()

        contentBuilder.addFunction(getForeignKeysFunction)

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
        definition: ColumnAnnotation
    ): PropertySpec {
        val elementType = columnElement.asType()
        if (!typeUtil.isAssignable(typeUtil.erasure(elementType), erasedColumnConfigType)) {
            throw ProcessingException(
                "It must be a ${columnConfigElement.qualifiedName}",
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

    private fun lazyTypeElement(kClass: KClass<*>) =
        lazy { elementUtil.getTypeElement(kClass.asClassName().canonicalName) }

    private fun lazyErasedType(element: TypeElement) =
        lazy { typeUtil.erasure(element.asType()) }

    companion object {
        private val GENERATED_GRAPH_CLASS =
            ClassName(Graph::class.asClassName().packageName(), "AppGraph")
    }
}