/*
 * MIT License
 *
 * Copyright (c) 2022 Touchlane LLC tech@touchlane.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

plugins {
    `kotlin-dsl`
}

group = "com.touchlane.gridpad.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.nexus.publish.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "gridpad.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("androidKotlin") {
            id = "gridpad.jetbrains.kotlin.android"
            implementationClass = "AndroidKotlinConventionPlugin"
        }
        register("androidKotlinExplicitApi") {
            id = "gridpad.jetbrains.kotlin.android.explicit"
            implementationClass = "AndroidKotlinExplicitApiConventionPlugin"
        }
        register("androidLibrary") {
            id = "gridpad.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("androidCompose") {
            id = "gridpad.android.compose"
            implementationClass = "AndroidComposeConventionPlugin"
        }
        register("publishNexusProject") {
            id = "gridpad.github.gradle-nexus.publish-plugin-project"
            implementationClass = "PublishNexusProjectConventionPlugin"
        }
        register("publishNexusModule") {
            id = "gridpad.github.gradle-nexus.publish-plugin-module"
            implementationClass = "PublishNexusModuleConventionPlugin"
        }
        register("androidJacoco") {
            id = "gridpad.jacoco"
            implementationClass = "AndroidJacocoConventionPlugin"
        }
        register("androidDetekt") {
            id = "gridpad.detekt"
            implementationClass = "AndroidDetektConventionPlugin"
        }
    }
}