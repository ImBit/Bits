import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

description = "🦑 Utility Plugin for Minecraft development."

tasks.withType<JavaCompile> {
    options.compilerArgs.add("-parameters")
}

tasks {
    jar { enabled = false }

    named<ShadowJar>("shadowJar") {
        archiveBaseName.set("Bits-Paper-Plugin")
        configurations = listOf(project.configurations.runtimeClasspath.get())
        from(sourceSets.main.get().output)
    }

    assemble { dependsOn("shadowJar") }
}
