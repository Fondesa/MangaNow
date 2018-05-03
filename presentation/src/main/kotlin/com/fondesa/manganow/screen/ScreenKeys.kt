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

package com.fondesa.manganow.screen

import android.os.Parcel
import android.os.Parcelable
import com.fondesa.screen.ScreenKey

enum class ScreenKeys(override val tag: String) : ScreenKey {

    SPLASH("splash"),

    LATEST("latest"),

    MANGA_LIST("manga_list"),

    SETTINGS("settings");

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(ordinal)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ScreenKeys> {
        override fun createFromParcel(parcel: Parcel): ScreenKeys {
            return ScreenKeys.values()[parcel.readInt()]
        }

        override fun newArray(size: Int): Array<ScreenKeys?> {
            return arrayOfNulls(size)
        }
    }
}