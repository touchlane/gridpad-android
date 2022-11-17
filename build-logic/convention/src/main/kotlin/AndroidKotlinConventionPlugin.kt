import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import com.touchlane.gridpad.configureKotlin
import org.gradle.api.Plugin
import org.gradle.api.Project

class AndroidKotlinConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.android")
            }

            extensions.findByType(ApplicationExtension::class.java)?.apply {
                configureKotlin(this)
            }
            extensions.findByType(LibraryExtension::class.java)?.apply {
                configureKotlin(this)
            }
        }
    }
}