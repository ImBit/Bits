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
    versionCatalogs {
        create("fabricLibs") {
            from(files("Fabric/gradle/libs.versions.toml"))
        }
        create("paperLibs") {
            from(files("Paper/gradle/libs.versions.toml"))
        }
        create("velocityLibs") {
            from(files("Velocity/gradle/libs.versions.toml"))
        }
    }
}



rootProject.name = "Bits"

include("API")

include("Paper")
include("Velocity")


include("Fabric")
include("Fabric:Server")
include("Fabric:Client")