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

loom {
    splitEnvironmentSourceSets()

    mods {
        create("bits") {
            sourceSet(sourceSets.main.get())
            sourceSet(sourceSets["client"])
//            sourceSet(sourceSets["server"])
        }
    }
}

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

    modImplementation("net.kyori:adventure-platform-fabric:6.8.0")

    modImplementation("me.lucko:fabric-permissions-api:0.5.0")

    api(project(":minecraft"))
    include(project(":minecraft"))
}

tasks {
    jar {
        from(sourceSets.main.get().output)
        from(sourceSets["client"].output)
    }
}

afterEvaluate {
    publishing {
        publications {
            named<MavenPublication>("maven") {
                artifacts.clear()
                artifact(tasks.named("remapJar"))
                artifact(tasks.named("sourcesJar"))
                artifact(tasks.named("javadocJar"))
            }
        }
    }
}