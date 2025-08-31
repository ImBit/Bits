description = "🦑 Utility Plugin for Minecraft development."

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

tasks.withType<Jar> {
    archiveBaseName.set("Bits")
}