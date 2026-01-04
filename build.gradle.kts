import net.ltgt.gradle.errorprone.errorprone

plugins {
    `java-library`
    `maven-publish`
    alias(libs.plugins.shadow)
    alias(libs.plugins.dotenv)
    alias(libs.plugins.errorprone)
}

group = "xyz.bitsquidd.bits"
version = "0.0.6"

allprojects {
    group = rootProject.group
    version = rootProject.version

    plugins.apply("java-library")
    plugins.apply("maven-publish")
    plugins.apply(rootProject.libs.plugins.shadow.get().pluginId)
    plugins.apply(rootProject.libs.plugins.errorprone.get().pluginId)

    dependencies {
        if (project.path != ":API") implementation(project(":API"))
        implementation(rootProject.libs.jb.annotations)
        implementation(rootProject.libs.jspecify)
        implementation(rootProject.libs.logger)
        implementation(rootProject.libs.brigadier)

        implementation(rootProject.libs.adventure)
        implementation(rootProject.libs.adventure.text.serializer.plain)

        errorprone(rootProject.libs.errorprone)
    }

    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
        maven("https://repo.papermc.io/repository/maven-public/")
    }

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.errorprone.isEnabled.set(true)
        options.errorprone.disableWarningsInGeneratedCode.set(true)
        options.errorprone.disableAllWarnings.set(true)
        options.errorprone.errorproneArgs.addAll(
            listOf(
                "-Xep:CollectionIncompatibleType:ERROR",
                "-Xep:EqualsIncompatibleType:ERROR",

                "-Xep:MissingOverride:ERROR",
                "-Xep:SelfAssignment:ERROR",
                "-Xep:StreamResourceLeak:ERROR",

                "-Xep:CanonicalDuration:OFF",
                "-Xep:InlineMeSuggester:OFF",
                "-Xep:ImmutableEnumChecker:OFF"
            )
        )
    }

    tasks.javadoc { options.encoding = "UTF-8" }
}

subprojects {
    extensions.configure<JavaPluginExtension>("java") {
        withSourcesJar()
        withJavadocJar()
    }
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(22))
    withSourcesJar()
    withJavadocJar()
}


// Scripts
subprojects {
    tasks.register("createPackageInfo") {
        doLast {
            val noReplaceString = "//NOREPLACE"

            val templateFile = rootProject.file("template/package-info.template")
            if (!templateFile.exists()) {
                throw GradleException("Template file 'package-info.template' not found!")
            }
            val template = templateFile.readText()

            val srcDir = file("src/main/java")
            if (!srcDir.exists()) {
                println("No src/main/java directory found in ${project.name}")
                return@doLast
            }

            srcDir.walk().forEach { file ->
                if (file.isDirectory && file != srcDir) {
                    val packageInfoFile = File(file, "package-info.java")

                    val hasJavaFiles = file.listFiles()?.any {
                        it.isFile && it.name.endsWith(".java") && it.name != "package-info.java"
                    } ?: false

                    if (hasJavaFiles) {
                        val relativePath = file.relativeTo(srcDir).path
                        val packagePath = relativePath.replace(File.separator, ".")

                        if (packageInfoFile.exists()) {
                            val currentContent = packageInfoFile.readText()
                            if (currentContent.startsWith(noReplaceString)) {
                                println("Skipping ${packagePath} (NOREPLACE found)")
                                return@forEach
                            }
                        }

                        val packageContent = template.replace("\${PACKAGE_NAME}", packagePath)
                        packageInfoFile.writeText(packageContent)
                        println("Created/Updated package-info.java for ${packagePath}")
                    } else {
                        if (packageInfoFile.exists()) {
                            packageInfoFile.delete()
                            println("Deleted package-info.java from empty directory: ${file.relativeTo(srcDir).path}")
                        }
                    }
                }
            }
        }
    }
}

tasks.register("createAllPackageInfo") {
    group = "build"
    description = "Creates package-info.java files in all subprojects"
    dependsOn(subprojects.map { it.tasks.named("createPackageInfo") })
}
