import xyz.bitsquidd.relocate

/*
 * This file is part of Bits, licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2024-2026 ImBit
 *
 * Enjoy the Bits and Bobs :)
 */

group = "com.github.imbit.bits"
version = property("bits_version") as String

plugins {
    `java-library`
    `maven-publish`
    alias(libs.plugins.kotlin)
    alias(libs.plugins.shadow)
    alias(libs.plugins.errorprone)
    alias(libs.plugins.imbit.convention)
}

allprojects {
    group = rootProject.group
    version = rootProject.version

    repositories {
        mavenLocal()
        mavenCentral()

        maven { url = uri("https://jitpack.io") }
    }

    dependencies {
        implementation(rootProject.libs.joml)
        implementation(rootProject.libs.logger)
        implementation(rootProject.libs.adventure.text.serializer.plain)

        shade(rootProject.libs.classgraph)

        api(rootProject.libs.gson)
        api(rootProject.libs.guava)
        api(rootProject.libs.brigadier)
        api(rootProject.libs.adventure)

        errorprone(rootProject.libs.errorprone)
    }

    relocate("io.github.classgraph" to "xyz.bitsquidd.internal.classgraph")
}
