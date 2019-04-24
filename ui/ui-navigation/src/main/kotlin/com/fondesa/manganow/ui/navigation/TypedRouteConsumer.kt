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

package com.fondesa.manganow.ui.navigation

import kotlin.reflect.KClass
import kotlin.reflect.full.cast

abstract class TypedRouteConsumer<T : Route>(private val routeClass: KClass<T>) : RouteConsumer {

    final override fun consume(route: Route) {
        if (routeClass.isInstance(route)) {
            consume(routeClass.cast(route))
        }
    }

    protected abstract fun consumeRoute(route: T)
}