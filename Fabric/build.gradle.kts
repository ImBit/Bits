/*
 * This file is part of Bits, licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2024-2026 ImBit
 *
 * Enjoy the Bits and Bobs :)
 */

plugins {
    alias(fabricLibs.plugins.fabric.loom)
}

allprojects {
    plugins.apply(rootProject.fabricLibs.plugins.fabric.loom.get().pluginId)

    repositories {
        mavenLocal()

        maven { url = uri("https://maven.fabricmc.net/") }

        mavenCentral()
    }

    dependencies {
        minecraft("com.mojang:minecraft:1.21.11")
        mappings(loom.officialMojangMappings())

        modImplementation(rootProject.fabricLibs.fabric.loader)
        modImplementation(rootProject.fabricLibs.fabric.api)
    }
}

subprojects {
    dependencies {
        implementation(project(":Fabric"))
    }
}
