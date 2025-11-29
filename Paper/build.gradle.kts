group = "xyz.bitsquidd.bits.paper"

plugins {
    alias(libs.plugins.paperweight.userdev)
}

allprojects {
    plugins.apply(rootProject.libs.plugins.paperweight.userdev.get().pluginId)

    dependencies {
        implementation(project(":Paper"))

        paperweight.paperDevBundle(rootProject.libs.versions.paper.api.get())
    }
}
