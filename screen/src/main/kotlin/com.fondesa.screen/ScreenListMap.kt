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

package com.fondesa.screen

class ScreenListMap(screenList: List<Pair<ScreenDefinition, ScreenClass>>) : ScreenMap {

    private val screenToDefinition = mutableMapOf<ScreenClass, ScreenDefinition>()
    private val definitionToScreen = mutableMapOf<ScreenDefinition, ScreenClass>()

    init {
        // Add the screens to the map.
        screenList.forEach { (definition, screen) ->
            screenToDefinition[screen] = definition
            definitionToScreen[definition] = screen
        }
    }

    override fun definitionOf(screenClass: ScreenClass): ScreenDefinition =
        screenToDefinition[screenClass] ?: throw IllegalArgumentException(
            "Cannot find a ${ScreenDefinition::class.java.name} for class ${screenClass.java.name}"
        )

    override fun screenOf(definition: ScreenDefinition): ScreenClass =
        definitionToScreen[definition] ?: throw IllegalArgumentException(
            "Cannot find a ${ScreenFragment::class.java.name} class for definition ${definition::class.java.name}"
        )
}

