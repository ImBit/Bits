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

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}
tasks.javadoc {
    options.encoding = "UTF-8"
}


publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])

            pom {
                name.set(project.name)
                description.set(project.description)
                url.set("https://github.com/ImBit/Bits")

                developers {
                    developer {
                        id.set("imbit")
                        name.set("ImBit")
                        email.set("bitsquiddbusiness@gmail.com")
                    }
                }
            }
        }
    }
}
