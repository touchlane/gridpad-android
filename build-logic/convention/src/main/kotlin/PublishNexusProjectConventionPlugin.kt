import io.github.gradlenexus.publishplugin.NexusPublishExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ExtraPropertiesExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.extra
import java.io.FileInputStream
import java.util.*

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

class PublishNexusProjectConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        val properties = PropertiesDelegate(target.extra)
        val secretPropsFile = target.file("local.properties")
        if (secretPropsFile.exists()) {
            val fileProperties = Properties()
            FileInputStream(secretPropsFile).use { stream -> fileProperties.load(stream) }
            fileProperties.forEach { key, value ->
                if (key is String) {
                    target.extra[key] = value
                }
            }
        } else {
            properties.ossrhUsername = System.getenv("OSSRH_USERNAME")
            properties.ossrhPassword = System.getenv("OSSRH_PASSWORD")
            properties.sonatypeStagingProfileId = System.getenv("SONATYPE_STAGING_PROFILE_ID")
            properties.signingKeyId = System.getenv("SIGNING_KEY_ID")
            properties.signingPassword = System.getenv("SIGNING_PASSWORD")
            properties.signingKeyId = System.getenv("SIGNING_KEY")
            properties.snapshot = System.getenv("SNAPSHOT")
        }
        with(target) {
            extensions.configure<NexusPublishExtension> {
                with(repositories.sonatype()) {
                    stagingProfileId.set(properties.sonatypeStagingProfileId)
                    username.set(properties.ossrhUsername)
                    password.set(properties.ossrhPassword)
                    nexusUrl.set(uri("https://s01.oss.sonatype.org/service/local/"))
                    snapshotRepositoryUrl.set(uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))
                }
            }
        }
    }
}

private class PropertiesDelegate(private val properties: ExtraPropertiesExtension) {

    var ossrhUsername: String
        get() {
            return this[KEY_OSSRH_USERNAME]
        }
        set(value) {
            this[KEY_OSSRH_USERNAME] = value
        }

    var ossrhPassword: String
        get() {
            return this[KEY_OSSRH_PASSWORD]
        }
        set(value) {
            this[KEY_OSSRH_PASSWORD] = value
        }

    var sonatypeStagingProfileId: String
        get() {
            return this[KEY_SONATYPE_STAGING_PROFILE_ID]
        }
        set(value) {
            this[KEY_SONATYPE_STAGING_PROFILE_ID] = value
        }

    var signingKeyId: String
        get() {
            return this[KEY_SIGNING_KEY_ID]
        }
        set(value) {
            this[KEY_SIGNING_KEY_ID] = value
        }

    var signingPassword: String
        get() {
            return this[KEY_SIGNING_PASSWORD]
        }
        set(value) {
            this[KEY_SIGNING_PASSWORD] = value
        }

    var signingKey: String
        get() {
            return this[KEY_SIGNING_KEY]
        }
        set(value) {
            this[KEY_SIGNING_KEY] = value
        }

    var snapshot: String
        get() {
            return this[KEY_SNAPSHOT]
        }
        set(value) {
            this[KEY_SNAPSHOT] = value
        }

    private operator fun set(key: String, value: String) {
        properties[key] = value
    }

    private operator fun get(key: String): String {
        if (!properties.has(key)) {
            return ""
        }
        val value = properties[key]
        return if (value == null || value !is String) {
            ""
        } else {
            value
        }
    }

    private companion object {
        const val KEY_OSSRH_USERNAME = "ossrhUsername"
        const val KEY_OSSRH_PASSWORD = "ossrhPassword"
        const val KEY_SONATYPE_STAGING_PROFILE_ID = "sonatypeStagingProfileId"
        const val KEY_SIGNING_KEY_ID = "signing.keyId"
        const val KEY_SIGNING_PASSWORD = "signing.password"
        const val KEY_SIGNING_KEY = "signing.key"
        const val KEY_SNAPSHOT = "snapshot"
    }
}