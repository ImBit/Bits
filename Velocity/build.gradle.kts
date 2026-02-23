/*
 * This file is part of Bits, licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2024-2026 ImBit
 *
 * Enjoy the Bits and Bobs :)
 */

allprojects {
    dependencies {
        implementation(project(":API"))

        implementation(rootProject.libs.velocity.api.get())
        annotationProcessor(rootProject.libs.velocity.api.get())
    }
}

subprojects {
    dependencies {
        implementation(project(":Velocity"))
    }
}
