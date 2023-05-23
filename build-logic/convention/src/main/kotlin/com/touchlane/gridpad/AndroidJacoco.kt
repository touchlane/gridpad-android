/*
 * MIT License
 *
 * Copyright (c) 2023 Touchlane LLC tech@touchlane.com
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
import groovy.xml.XmlSlurper
import groovy.xml.slurpersupport.NodeChild
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.kotlin.dsl.extra
import org.gradle.testing.jacoco.plugins.JacocoPluginExtension
import org.gradle.testing.jacoco.tasks.JacocoReport
import java.io.File
import java.util.*
import kotlin.math.roundToInt

private val excludedFiles = mutableSetOf(
    "**/R.class",
    "**/R$*.class",
    "**/*\$ViewInjector*.*",
    "**/*\$ViewBinder*.*",
    "**/BuildConfig.*",
    "**/Manifest*.*",
    "**/*Factory*",
    "**/*_MembersInjector*",
    "**/*Module*",
    "**/*Component*",
    "**android**",
    "**/BR.class"
)

internal val limits = mutableMapOf(
    "instruction" to 0.0,
    "branch" to 0.0,
    "line" to 0.0,
    "complexity" to 0.0,
    "method" to 0.0,
    "class" to 0.0
)

@Suppress("UnstableApiUsage")
internal fun Project.configureAndroidJacoco(
    extension: CommonExtension<*, *, *, *>, jacoco: JacocoPluginExtension
) = afterEvaluate {

    val buildTypes = extension.buildTypes.map { type -> type.name }
    var productFlavors = extension.productFlavors.map { flavor -> flavor.name }

    if (productFlavors.isEmpty()) {
        productFlavors = productFlavors + ""
    }

    productFlavors.forEach { flavorName ->
        buildTypes.forEach { buildTypeName ->
            val sourceName: String
            val sourcePath: String

            if (flavorName.isEmpty()) {
                sourceName = buildTypeName
                sourcePath = buildTypeName
            } else {
                sourceName = "${flavorName}${buildTypeName.capitalize(Locale.ENGLISH)}"
                sourcePath = "${flavorName}/${buildTypeName}"
            }

            val testTaskName = "test${sourceName.capitalize(Locale.ENGLISH)}UnitTest"

            registerCodeCoverageTask(
                jacoco = jacoco,
                testTaskName = testTaskName,
                sourceName = sourceName,
                sourcePath = sourcePath,
                flavorName = flavorName,
                buildTypeName = buildTypeName
            )
        }
    }
}

private fun Project.registerCodeCoverageTask(
    jacoco: JacocoPluginExtension,
    testTaskName: String,
    sourceName: String,
    sourcePath: String,
    flavorName: String,
    buildTypeName: String
) {
    tasks.register("${testTaskName}Coverage", JacocoReport::class.java) {
        dependsOn(testTaskName)
        group = "Reporting"
        description =
            "Generate Jacoco coverage reports on the ${sourceName.capitalize(Locale.ENGLISH)} build."

        val javaDirectories = fileTree(
            "${project.buildDir}/intermediates/classes/${sourcePath}"
        ) { exclude(excludedFiles) }

        val kotlinDirectories = fileTree(
            "${project.buildDir}/tmp/kotlin-classes/${sourcePath}"
        ) { exclude(excludedFiles) }

        val coverageSrcDirectories = listOf(
            "src/main/java",
            "src/$flavorName/java",
            "src/$buildTypeName/java",
            "src/main/kotlin",
            "src/$flavorName/kotlin",
            "src/$buildTypeName/kotlin"
        )

        classDirectories.setFrom(files(javaDirectories, kotlinDirectories))
        additionalClassDirs.setFrom(files(coverageSrcDirectories))
        sourceDirectories.setFrom(files(coverageSrcDirectories))
        executionData.setFrom(
            files("${project.buildDir}/jacoco/${testTaskName}.exec")
        )

        reports {
            xml.required.set(true)
            html.required.set(true)
        }

        doLast {
            jacocoTestReport(jacoco, "${testTaskName}Coverage")
        }
    }
}

@Suppress("UNCHECKED_CAST")
private fun Project.jacocoTestReport(jacoco: JacocoPluginExtension, testTaskName: String) {
    val reportsDirectory = jacoco.reportsDirectory.asFile.get()
    val report = file("$reportsDirectory/${testTaskName}/${testTaskName}.xml")

    logger.lifecycle("Checking coverage results: $report")

    val metrics = report.extractTestsCoveredByType()
    val limits = project.extra["limits"] as Map<String, Double>

    val failures = metrics.filter { entry ->
        entry.value < limits[entry.key]!!
    }.map { entry ->
        "- ${entry.key} coverage rate is: ${entry.value}%, minimum is ${limits[entry.key]}%"
    }

    if (failures.isNotEmpty()) {
        logger.quiet("------------------ Code Coverage Failed -----------------------")
        failures.forEach { logger.quiet(it) }
        logger.quiet("---------------------------------------------------------------")
        throw GradleException("Code coverage failed")
    }

    logger.quiet("------------------ Code Coverage Success -----------------------")
    metrics.forEach { entry ->
        logger.quiet("- ${entry.key} coverage rate is: ${entry.value}%")
    }
    logger.quiet("---------------------------------------------------------------")
}

@Suppress("UNCHECKED_CAST")
private fun File.extractTestsCoveredByType(): Map<String, Double> {
    val xmlReader = XmlSlurper().apply {
        setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false)
        setFeature("http://apache.org/xml/features/disallow-doctype-decl", false)
    }

    val counterNodes: List<NodeChild> = xmlReader
        .parse(this).parent()
        .children()
        .filter {
            (it as NodeChild).name() == "counter"
        } as List<NodeChild>

    return counterNodes.associate { nodeChild ->
        val type = nodeChild.attributes()["type"].toString().toLowerCase(Locale.ENGLISH)

        val covered = nodeChild.attributes()["covered"].toString().toDouble()
        val missed = nodeChild.attributes()["missed"].toString().toDouble()
        val percentage = ((covered / (covered + missed)) * 10000.0).roundToInt() / 100.0

        Pair(type, percentage)
    }
}