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

package com.fondesa.manganow.ui.recyclerview

import android.view.ViewGroup
import com.fondesa.manganow.ui.util.inflateChild

/**
 * Type of [InteractiveRecyclerViewHolder] used to show the progress in a [PagingRecyclerViewAdapter].
 * The progress will be shown when the page size is reached.
 */
class PagingRecyclerViewHolder(parent: ViewGroup) :
    InteractiveRecyclerViewHolder(parent.inflateChild(R.layout.row_progress))