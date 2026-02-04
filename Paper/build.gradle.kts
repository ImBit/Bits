plugins {
    alias(libs.plugins.paperweight.userdev)
}

allprojects {
    plugins.apply(rootProject.libs.plugins.paperweight.userdev.get().pluginId)

    dependencies {
        paperweight.paperDevBundle(rootProject.libs.versions.paper.api.get())
    }
}

subprojects {
    dependencies {
        implementation(project(":Paper"))
    }
}
