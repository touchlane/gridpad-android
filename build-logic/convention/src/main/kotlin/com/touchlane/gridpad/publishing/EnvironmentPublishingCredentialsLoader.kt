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

internal class EnvironmentPublishingCredentialsLoader :
    PublishingCredentialsLoader {

    override val name: String = "Environment Loader"

    override fun canLoad(): Boolean {
        return !System.getenv(KEY_OSSRH_USERNAME).isNullOrBlank()
    }

    override fun loadTo(target: PublishingCredentialsDelegate) {
        target.ossrhUsername = System.getenv(KEY_OSSRH_USERNAME) ?: ""
        target.ossrhPassword = System.getenv(KEY_OSSRH_PASSWORD) ?: ""
        target.sonatypeStagingProfileId = System.getenv(KEY_SONATYPE_STAGING_PROFILE_ID) ?: ""
        target.signingKeyId = System.getenv(KEY_SIGNING_KEY_ID) ?: ""
        target.signingPassword = System.getenv(KEY_SIGNING_PASSWORD) ?: ""
        target.signingKey = System.getenv(KEY_SIGNING_KEY) ?: ""
    }

    private companion object {
        const val KEY_OSSRH_USERNAME = "OSSRH_USERNAME"
        const val KEY_OSSRH_PASSWORD = "OSSRH_PASSWORD"
        const val KEY_SONATYPE_STAGING_PROFILE_ID = "SONATYPE_STAGING_PROFILE_ID"
        const val KEY_SIGNING_KEY_ID = "SIGNING_KEY_ID"
        const val KEY_SIGNING_PASSWORD = "SIGNING_PASSWORD"
        const val KEY_SIGNING_KEY = "SIGNING_KEY"
    }
}