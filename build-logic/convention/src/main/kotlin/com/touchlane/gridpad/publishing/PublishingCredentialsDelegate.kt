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

package com.touchlane.gridpad.publishing

import org.gradle.api.Project
import org.gradle.api.plugins.ExtraPropertiesExtension
import org.gradle.kotlin.dsl.extra

internal class PublishingCredentialsDelegate private constructor(private val properties: ExtraPropertiesExtension) {

    val isExists: Boolean
        get() = signingKey.isNotBlank()

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

    operator fun set(key: String, value: String) {
        properties[key] = value
    }

    operator fun get(key: String): String {
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

    companion object {
        private const val KEY_OSSRH_USERNAME = "ossrhUsername"
        private const val KEY_OSSRH_PASSWORD = "ossrhPassword"
        private const val KEY_SONATYPE_STAGING_PROFILE_ID = "sonatypeStagingProfileId"
        private const val KEY_SIGNING_KEY_ID = "signing.keyId"
        private const val KEY_SIGNING_PASSWORD = "signing.password"
        private const val KEY_SIGNING_KEY = "signing.key"

        fun from(target: Project): PublishingCredentialsDelegate {
            val loaders = listOf(
                FilePublishingCredentialsLoader(target.file("local.properties")),
                EnvironmentPublishingCredentialsLoader()
            )
            val credentials = PublishingCredentialsDelegate(target.extra)
            loaders.forEach {
                if (it.canLoad()) {
                    it.loadTo(credentials)
                    println("Load credentials from ${it.name} completed.")
                    return@forEach
                } else {
                    println("Load credentials from ${it.name} skipped.")
                }
            }
            return credentials
        }
    }
}