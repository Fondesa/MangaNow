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

package com.fondesa.manganow.gradle

import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project

class CommonAndroidPlugin implements Plugin<Project> {

    @Override
    void apply(Project target) {
        target.with {
            apply plugin: 'kotlin-android'
            apply plugin: 'kotlin-kapt'
            apply plugin: 'kotlin-android-extensions'

            android {
                compileSdkVersion androidConfig.compileSdk
                defaultConfig {
                    minSdkVersion androidConfig.minSdk
                    targetSdkVersion androidConfig.targetSdk
                    multiDexEnabled true
                    vectorDrawables.useSupportLibrary = true

                    testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"
                    testInstrumentationRunnerArguments clearPackageData: 'true'
                }

                compileOptions {
                    targetCompatibility JavaVersion.VERSION_1_8
                    sourceCompatibility JavaVersion.VERSION_1_8
                }

                testOptions {
                    execution 'ANDROIDX_TEST_ORCHESTRATOR'
                    animationsDisabled = true
                    unitTests.all {
                        testLogging {
                            events "passed", "skipped", "failed"
                        }
                    }
                }

                sourceSets.forEach {
                    it.java.srcDirs += "src/${it.name}/kotlin"
                }

                androidExtensions {
                    experimental = true
                }
            }

            kapt {
                useBuildCache = true
                // All the kapt arguments are ignored if they can't be handled by the single module.
                arguments {
                    // Arguments needed by Dagger.
                    arg("dagger.formatGeneratedSource", "disabled")
                    arg("dagger.gradle.incremental")
                    // Arguments needed by DatabaseProcessor.
                    arg("androidProcessing")
                }
            }

            dependencies {
                deps.multiDex

                androidTestUtil deps.androidTestOrchestrator
            }
        }
    }
}