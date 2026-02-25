/*
 * This file is part of Bits, licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2024-2026 ImBit
 *
 * Enjoy the Bits and Bobs :)
 */

plugins {
    alias(libs.plugins.paperweight.userdev)
}

allprojects {
    plugins.apply(rootProject.libs.plugins.paperweight.userdev.get().pluginId)

    dependencies {
        implementation(project(":API", configuration = "shadow"))

        paperweight.paperDevBundle(rootProject.libs.versions.paper.api.get())
    }
}

subprojects {
    dependencies {
        implementation(project(":Paper", configuration = "shadow"))
    }
}
