import xyz.bitsquidd.includeLibrary

/*
 * This file is part of Bits, licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2024-2026 ImBit
 *
 * Enjoy the Bits and Bobs :)
 */

plugins {
    alias(paperLibs.plugins.paperweight.userdev)
}

allprojects {
    plugins.apply(rootProject.paperLibs.plugins.paperweight.userdev.get().pluginId)

    repositories {
        maven { url = uri("https://repo.papermc.io/repository/maven-public/") }
    }

    dependencies {
        includeLibrary(project(":api"))

        paperweight.paperDevBundle(rootProject.paperLibs.versions.paper.api.get())
    }
}

subprojects {
    dependencies {
        implementation(project(":paper"))
    }
}
