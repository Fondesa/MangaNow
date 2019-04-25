/*
 * Copyright (c) 2019 Fondesa
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

package com.fondesa.manganow.navigation.impl

import com.fondesa.manganow.navigation.api.Route
import com.fondesa.manganow.navigation.api.RouteConsumer
import com.fondesa.manganow.navigation.api.Router
import javax.inject.Inject

class RouterImpl @Inject constructor(private val consumerMap: Map<Class<out Route>, @JvmSuppressWildcards RouteConsumer>) :
    Router {

    override fun navigate(route: Route) {
        val routeClass = route::class.java
        val consumer = consumerMap[routeClass] ?: throw IllegalArgumentException(
            "No ${RouteConsumer::class.java.name} found for route ${routeClass.name}."
        )
        consumer.consume(route)
    }
}