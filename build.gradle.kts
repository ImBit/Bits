plugins {
    `java-library`
    `maven-publish`
    alias(libs.plugins.shadow)
    alias(libs.plugins.paperweight.userdev)
}

group = "xyz.bitsquidd"
version = "0.0.4-SNAPSHOT"

allprojects {
    group = rootProject.group
    version = rootProject.version

    plugins.apply(rootProject.libs.plugins.paperweight.userdev.get().pluginId)

    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
        maven("https://repo.papermc.io/repository/maven-public/")
    }

    dependencies {
        paperweight.paperDevBundle(rootProject.libs.versions.paper.api.get())
    }

    tasks.withType<JavaCompile> { options.encoding = "UTF-8" }
    tasks.javadoc { options.encoding = "UTF-8" }
}



java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
    withSourcesJar()
    withJavadocJar()
}
