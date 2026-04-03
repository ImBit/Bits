/*
 * This file is part of Bits, licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2024-2026 ImBit
 *
 * Enjoy the Bits and Bobs :)
 */

group = "xyz.bitsquidd.bits"
version = property("bits_version") as String

plugins {
    alias(libs.plugins.bit.convention)
}

allprojects {
    group = rootProject.group
    version = rootProject.version

    repositories {
        mavenCentral()
    }

    publishing {
        repositories {
            maven {
                url = uri("https://repo.bitsquidd.xyz/repository/bit/")
                credentials {
                    username = System.getenv("MAVEN_USERNAME")
                    password = System.getenv("MAVEN_PASSWORD")
                }
            }
        }
    }
}