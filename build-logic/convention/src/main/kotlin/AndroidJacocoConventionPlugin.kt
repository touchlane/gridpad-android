import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import com.touchlane.gridpad.configureAndroidJacoco
import com.touchlane.gridpad.limits
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.extra
import org.gradle.testing.jacoco.plugins.JacocoPluginExtension

class AndroidJacocoConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("jacoco")
            }
            extra.set("limits", limits)
            val jacoco = extensions.getByType(JacocoPluginExtension::class.java)
            extensions.findByType(ApplicationExtension::class.java)?.apply {
                configureAndroidJacoco(this, jacoco)
            }
            extensions.findByType(LibraryExtension::class.java)?.apply {
                configureAndroidJacoco(this, jacoco)
            }
        }
    }
}