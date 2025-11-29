group = "xyz.bitsquidd.bits.paper"

plugins {
    alias(libs.plugins.paperweight.userdev)
}

plugins.apply(rootProject.libs.plugins.paperweight.userdev.get().pluginId)

dependencies {
    paperweight.paperDevBundle(rootProject.libs.versions.paper.api.get())
}