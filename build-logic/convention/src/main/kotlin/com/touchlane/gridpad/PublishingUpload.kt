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

import com.android.build.gradle.LibraryExtension
import com.android.builder.model.SourceProvider
import com.touchlane.gridpad.publishing.PublishingCredentialsDelegate
import com.touchlane.gridpad.publishing.PublishingProperties
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.api.tasks.TaskProvider
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.*
import org.gradle.plugins.signing.SigningExtension

internal fun Project.configurePublishingUpload(
    extension: PublishingExtension
) = extension.apply {
    val javadoc = javadoc()

    val properties = PublishingProperties.load(this@configurePublishingUpload)

    publications {
        create<MavenPublication>(properties.publicationName) {
            from(components["release"])
            groupId = properties.groupId
            artifactId = properties.artifactId
            version = properties.version

            artifact(javadoc.get())

            pom {
                name.set("Touchlane ${project.name}")
                description.set(properties.pomDescription)
                url.set(properties.pomUrl)
                licenses {
                    license {
                        name.set("MIT")
                        url.set("https://choosealicense.com/licenses/mit/")
                    }
                }
                developers {
                    developer {
                        id.set("touchlane")
                        name.set("Touchlane LLC")
                        email.set("tech.touchlane@gmail.com")
                    }
                }
                scm {
                    connection.set(properties.scmConnection)
                    developerConnection.set(properties.scmDeveloperConnection)
                    url.set(properties.scmUrl)
                }
            }
        }
    }

    val credentials = PublishingCredentialsDelegate.from(rootProject)

    configure<SigningExtension> {
        if (credentials.isExists) {
            if (credentials.signingKeyId.isNotBlank()) {
                useInMemoryPgpKeys(
                    credentials.signingKeyId,
                    credentials.signingKey,
                    credentials.signingPassword
                )
            } else {
                useInMemoryPgpKeys(
                    credentials.signingKey,
                    credentials.signingPassword
                )
            }
            sign(extension.publications[properties.publicationName])
            sign(configurations["archives"])
        } else {
            println("Signing skipped: not found credentials")
        }
    }
}

internal fun Project.javadoc(): TaskProvider<Jar> {
    val javadocJar: TaskProvider<Jar> by tasks.registering(Jar::class) {
        archiveClassifier.set("javadoc")

        val dokkaJavadocTask = tasks.getByName("dokkaJavadoc")

        from(dokkaJavadocTask)
        dependsOn(dokkaJavadocTask)
    }
    return javadocJar
}