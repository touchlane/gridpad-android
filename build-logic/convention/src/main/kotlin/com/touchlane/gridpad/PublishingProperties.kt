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

import org.gradle.api.Project
import java.io.FileInputStream
import java.util.*

data class PublishingProperties(val groupId: String, val artifactId: String, val version: String) {
    companion object {
        fun load(target: Project): PublishingProperties {
            val secretPropsFile = target.file("artifact.properties")
            val properties = mutableMapOf<String, Any>()
            if (secretPropsFile.exists()) {
                val fileProperties = Properties()
                FileInputStream(secretPropsFile).use { stream -> fileProperties.load(stream) }
                fileProperties.forEach { key, value ->
                    if (key is String) {
                        properties[key] = value
                    }
                }
            }
            return PublishingProperties(
                groupId = properties.stringOrEmpty("groupId"),
                artifactId = properties.stringOrEmpty("artifactId"),
                version = properties.stringOrEmpty("version")
            )
        }
    }
}

private fun Map<String, Any>.stringOrEmpty(key: String): String {
    val value = this[key]
    return if (value is String) {
        return value
    } else {
        ""
    }
}