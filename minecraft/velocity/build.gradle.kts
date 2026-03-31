import xyz.bitsquidd.includeLibrary

/*
 * This file is part of Bits, licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2024-2026 ImBit
 *
 * Enjoy the Bits and Bobs :)
 */

allprojects {
    repositories {
        maven { url = uri("https://repo.papermc.io/repository/maven-public/") }
    }

    dependencies {
        includeLibrary(project(":api"))

        implementation(rootProject.velocityLibs.velocity.api.get())
        annotationProcessor(rootProject.velocityLibs.velocity.api.get())
    }
}

subprojects {
    dependencies {
        includeLibrary(project(":velocity"))
    }
}
