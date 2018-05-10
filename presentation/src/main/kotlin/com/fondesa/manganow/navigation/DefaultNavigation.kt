///*
// * Copyright (c) 2018 Fondesa
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *     http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package com.fondesa.manganow.navigation
//
//import android.support.annotation.IdRes
//import com.fondesa.manganow.R
//import javax.inject.Inject
//
//class DefaultNavigation @Inject constructor() : Navigation {
//
//    @IdRes
//    override fun rootItemId() = R.id.section_home
//
////    override fun configurationOfItem(@IdRes itemId: Int): ScreenConfiguration? = when (itemId) {
////        R.id.section_home -> screenConfiguration<LatestScreen>(ScreenKeys.LATEST)
////        R.id.section_list -> screenConfiguration<MangaListScreen>(ScreenKeys.MANGA_LIST)
////        R.id.section_settings ->  screenConfiguration<SettingsScreen>(ScreenKeys.SETTINGS)
////        else -> null
////    }
//}