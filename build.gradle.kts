plugins {
    `java-library`
    `maven-publish`
    alias(libs.plugins.shadow)
    alias(libs.plugins.dotenv)
}

group = "xyz.bitsquidd.bits"
version = "0.0.6"

allprojects {
    group = rootProject.group
    version = rootProject.version

    plugins.apply("java-library")
    plugins.apply("maven-publish")
    plugins.apply(rootProject.libs.plugins.shadow.get().pluginId)

    dependencies {
        if (project.path != ":API") implementation(project(":API"))
        implementation(rootProject.libs.jb.annotations)
        implementation(rootProject.libs.jspecify)
        implementation(rootProject.libs.adventure)
        implementation(rootProject.libs.logger)
    }

    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
        maven("https://repo.papermc.io/repository/maven-public/")
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
