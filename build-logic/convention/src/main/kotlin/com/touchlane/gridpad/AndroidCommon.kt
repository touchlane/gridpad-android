package com.touchlane.gridpad

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project

@Suppress("UnstableApiUsage")
internal fun Project.configureAndroidCommon(
    extension: CommonExtension<*, *, *, *>,
) {
    extension.apply {
        compileSdk = 33

        defaultConfig {
            minSdk = 24
            vectorDrawables {
                useSupportLibrary = true
            }
        }

        buildFeatures {
            compose = true
            aidl = false
            buildConfig = false
            renderScript = false
            shaders = false
        }

        packagingOptions {
            resources {
                excludes += "/META-INF/{AL2.0,LGPL2.1}"
            }
        }
    }
}