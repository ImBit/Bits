plugins {
    `java-library`
    `maven-publish`
    alias(libs.plugins.shadow)
    alias(libs.plugins.paperweight.userdev)
}

group = "xyz.bitsquidd"
version = "0.1-SNAPSHOT"
description = "Bits Plugin!"

repositories {
    mavenLocal()
    mavenCentral()
    gradlePluginPortal()

    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    paperweight.paperDevBundle(libs.versions.paper.api.get())
}