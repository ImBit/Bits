/*
 * This file is part of Bits, licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2024-2026 ImBit
 *
 * Enjoy the Bits and Bobs :)
 */

pluginManagement {
    repositories {
        mavenLocal()

        maven {
            name = "Fabric"
            url = uri("https://maven.fabricmc.net/")
        }

        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        mavenLocal()

        // PaperMC - required for paperweight dev bundle
        maven { url = uri("https://repo.papermc.io/repository/maven-public/") }
        maven { url = uri("https://jitpack.io") }

        mavenCentral()
        gradlePluginPortal()
    }
}

rootProject.name = "Bits"

include("API")

include("Paper")
include("Velocity")


include("Fabric")
include("Fabric:Server")
include("Fabric:Client")