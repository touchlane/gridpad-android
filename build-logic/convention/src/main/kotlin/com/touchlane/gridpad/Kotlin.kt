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

package com.touchlane.gridpad

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions

internal fun Project.configureKotlin(
    commonExtension: CommonExtension<*, *, *, *>,
) = commonExtension.apply {
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        if (findProperty("gridpad.enableComposeCompilerReports") != null) {
            freeCompilerArgs = listOf(
                *freeCompilerArgs.toTypedArray(), *composeMetricsArgs().toTypedArray()
            )
        }
        jvmTarget = JavaVersion.VERSION_11.toString()
    }
}

private fun CommonExtension<*, *, *, *>.kotlinOptions(block: KotlinJvmOptions.() -> Unit) {
    (this as ExtensionAware).extensions.configure("kotlinOptions", block)
}

private fun Project.composeMetricsArgs(): List<String> {
    val metricsPath = rootProject.composeMetricsPath()
    return listOf(
        "-P",
        "$ARG_COMPOSE_REPORTS_DESTINATION=$metricsPath",
        "-P",
        "$ARG_COMPOSE_METRICS_DESTINATION=$metricsPath"
    )
}

private fun Project.composeMetricsPath(): String {
    return "${buildDir.absolutePath}/compose_metrics"
}

private const val ARG_COMPOSE_REPORTS_DESTINATION =
    "plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination"
private const val ARG_COMPOSE_METRICS_DESTINATION =
    "plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination"