import com.touchlane.gridpad.SigningPropertiesDelegate
import io.github.gradlenexus.publishplugin.NexusPublishExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
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
        val properties = SigningPropertiesDelegate(target.extra)
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
            properties.ossrhUsername = System.getenv("OSSRH_USERNAME") ?: ""
            properties.ossrhPassword = System.getenv("OSSRH_PASSWORD") ?: ""
            properties.sonatypeStagingProfileId = System.getenv("SONATYPE_STAGING_PROFILE_ID") ?: ""
            properties.signingKeyId = System.getenv("SIGNING_KEY_ID") ?: ""
            properties.signingPassword = System.getenv("SIGNING_PASSWORD") ?: ""
            properties.signingKeyId = System.getenv("SIGNING_KEY") ?: ""
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