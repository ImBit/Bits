/*
 * This file is part of Bits, licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2024-2026 ImBit
 *
 * Enjoy the Bits and Bobs :)
 */

import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import net.ltgt.gradle.errorprone.errorprone
import xyz.bitsquidd.bits.util.BuildUtil

plugins {
    `java-library`
    `maven-publish`
    alias(libs.plugins.kotlin)
    alias(libs.plugins.shadow)
    alias(libs.plugins.errorprone)
}

group = "xyz.bitsquidd.bits"

allprojects {
    group = rootProject.group
    version = project.property("bits_version") as String

    plugins.apply("java-library")
    plugins.apply("maven-publish")
    plugins.apply(rootProject.libs.plugins.kotlin.get().pluginId)
    plugins.apply(rootProject.libs.plugins.shadow.get().pluginId)
    plugins.apply(rootProject.libs.plugins.errorprone.get().pluginId)

    dependencies {
        compileOnly(rootProject.libs.jb.annotations)

        implementation(rootProject.libs.joml)
        implementation(rootProject.libs.logger)
        implementation(rootProject.libs.adventure.text.serializer.plain)

        api(rootProject.libs.brigadier)
        api(rootProject.libs.adventure)

        errorprone(rootProject.libs.errorprone)
    }

    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
        maven("https://repo.papermc.io/repository/maven-public/")
    }

    tasks {
        jar { finalizedBy(shadowJar) }
        assemble { dependsOn(shadowJar) }
        javadoc { options.encoding = "UTF-8" }

        withType<JavaCompile> {
            options.encoding = "UTF-8"
            options.errorprone.isEnabled.set(true)
            options.errorprone.disableWarningsInGeneratedCode.set(true)
            options.errorprone.disableAllWarnings.set(true)
            options.errorprone.errorproneArgs.addAll(
                listOf(
                    "-Xep:CollectionIncompatibleType:ERROR",
                    "-Xep:EqualsIncompatibleType:ERROR",

                    "-Xep:MissingOverride:ERROR",
                    "-Xep:SelfAssignment:ERROR",
                    "-Xep:StreamResourceLeak:ERROR",

                    "-Xep:CanonicalDuration:OFF",
                    "-Xep:InlineMeSuggester:OFF",
                    "-Xep:ImmutableEnumChecker:OFF"
                )
            )
        }

        named<ShadowJar>("shadowJar") {
            archiveClassifier.set("")          // Omits classifier from jar name.
            manifest { attributes["Implementation-Version"] = project.version }
            from(sourceSets.main.get().output)
        }
    }
}

subprojects {
    extensions.configure<JavaPluginExtension>("java") {
        withSourcesJar()
        // Javadocs break JitPack generation. TODO: Fix this.
        if (project.name != "Paper") {
            withJavadocJar()
        }
    }

    publishing {
        publications {
            create<MavenPublication>("mavenJava") {
                groupId = project.group.toString()
                artifactId = project.name
                version = project.version.toString()
                from(components["java"])
            }
        }
    }
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
    withSourcesJar()
    withJavadocJar()
}

kotlin {
    jvmToolchain(21)
}

BuildUtil.apply { init() }

