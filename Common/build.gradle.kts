import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

description = "🦑 Utility Plugin for Minecraft development."

plugins {
    alias(libs.plugins.shadow)
}

repositories {
}

dependencies {
    implementation(project(":API"))
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


tasks {
    jar { enabled = false }

    named<ShadowJar>("shadowJar") {
        archiveBaseName.set("Bits-Plugin")
        configurations = listOf(project.configurations.runtimeClasspath.get())
        from(sourceSets.main.get().output)
    }

    named<Jar>("sourcesJar") {
        dependsOn("processPluginYml")
    }

    assemble { dependsOn("shadowJar") }
}