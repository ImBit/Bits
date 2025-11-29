group = "xyz.bitsquidd.bits.paper"


allprojects {
    dependencies {
        implementation(project(":Velocity"))

        implementation(rootProject.libs.velocity.api.get())
        annotationProcessor(rootProject.libs.velocity.api.get())
    }
}
