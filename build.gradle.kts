plugins {
    `java-library`
    `maven-publish`
    alias(libs.plugins.shadow)
    alias(libs.plugins.paperweight.userdev)
}

group = "xyz.bitsquidd"
version = "0.0.4-SNAPSHOT"
description = "🦑 Utility plugin for Minecraft development."

repositories {
    mavenLocal()
    mavenCentral()
    gradlePluginPortal()

    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    paperweight.paperDevBundle(libs.versions.paper.api.get())
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
    withSourcesJar()
    withJavadocJar()
}

tasks.register<Copy>("processPluginYml") {
    val templateFile = file("src/main/resources/plugin.yml.template")
    val outputFile = file("src/main/resources/plugin.yml")
    from(templateFile)
    into(outputFile.parent)
    rename { "plugin.yml" }
    expand("version" to project.version)
}

tasks.processResources {
    dependsOn("processPluginYml")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}
tasks.javadoc {
    options.encoding = "UTF-8"
}
