/*
 * This file is part of Bits, licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2024-2026 ImBit
 *
 * Enjoy the Bits and Bobs :)
 */

plugins {
    alias(libs.plugins.fabric.loom)
}

allprojects {
    plugins.apply(rootProject.libs.plugins.fabric.loom.get().pluginId)

    dependencies {
        minecraft("com.mojang:minecraft:1.21.11")

        mappings(loom.officialMojangMappings())

        modImplementation(rootProject.libs.fabric.loader)
        modImplementation(rootProject.libs.fabric.api)
    }
}

subprojects {
    dependencies {
        implementation(project(":Fabric", configuration = "shadow"))
    }
}
