import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import xyz.bitsquidd.util.shadeApi

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

    shadeApi(project(":minecraft"))
}

tasks {
    val mergedJar by registering(ShadowJar::class) {
        archiveClassifier.set("merged")
        from(sourceSets.main.get().output)
        from(sourceSets["client"].output)
        from(zipTree(project(":minecraft").tasks.named<ShadowJar>("shadowJar").flatMap { it.archiveFile }))
    }

    remapJar {
        dependsOn(mergedJar)
        inputFile.set(mergedJar.flatMap { it.archiveFile })
        archiveClassifier.set("")
    }

    processResources {
        filteringCharset = "UTF-8"

        filesMatching("fabric.mod.json") {
            expand(
                "version" to project.version
            )
        }
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