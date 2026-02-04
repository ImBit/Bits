allprojects {
    dependencies {
        implementation(rootProject.libs.velocity.api.get())
        annotationProcessor(rootProject.libs.velocity.api.get())
    }
}

subprojects {
    dependencies {
        implementation(project(":Velocity"))
    }
}

tasks.withType<Jar> {
    archiveBaseName.set("Bits-Velocity")
}

