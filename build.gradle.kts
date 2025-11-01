plugins {
    `java-library`
    `maven-publish`
    alias(libs.plugins.shadow)
    alias(libs.plugins.paperweight.userdev)
    alias(libs.plugins.dotenv)
}

group = "xyz.bitsquidd"
version = "0.0.6"

allprojects {
    group = rootProject.group
    version = rootProject.version

    plugins.apply(rootProject.libs.plugins.paperweight.userdev.get().pluginId)
    plugins.apply("java-library")
    plugins.apply("maven-publish")

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

subprojects {
    extensions.configure<JavaPluginExtension>("java") {
        withSourcesJar()
        withJavadocJar()
    }
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
    withSourcesJar()
    withJavadocJar()
}


