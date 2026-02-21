package xyz.bitsquidd.bits.util

import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.tasks.TaskProvider
import java.io.File

object BuildUtil {

    fun Project.init(): TaskProvider<Task> {
        subprojects.forEach { sub ->
            with(BuildUtil) { sub.registerCreatePackageInfoTask() }
        }
        return registerCreateAllPackageInfoTask()
    }

    fun Project.registerCreatePackageInfoTask(): TaskProvider<Task> =
        tasks.register("createPackageInfo") {
            val srcDir = project.file("src/main/java")
            val templateFile = rootProject.file("template/package-info.template")

            doLast {
                if (!srcDir.exists()) return@doLast
                if (!templateFile.exists()) throw GradleException("Template file 'package-info.template' not found!")

                val template = templateFile.readText()

                srcDir.walkTopDown()
                    .filter { it.isDirectory && it != srcDir }
                    .forEach { dir ->
                        val javaFiles = dir.listFiles()?.filter { it.isFile && it.extension == "java" && it.name != "package-info.java" } ?: emptyList()
                        val packageInfoFile = File(dir, "package-info.java")
                        val isNoReplace = packageInfoFile.exists() &&
                          packageInfoFile.useLines { it.firstOrNull()?.startsWith("//NOREPLACE") == true }

                        when {
                            isNoReplace -> return@forEach
                            javaFiles.isNotEmpty() -> {
                                val packageName = dir.relativeTo(srcDir).path.replace(File.separatorChar, '.')
                                packageInfoFile.writeText(template.replace("\${PACKAGE_NAME}", packageName))
                            }

                            packageInfoFile.exists() -> packageInfoFile.delete()
                        }
                    }
            }
        }

    fun Project.registerCreateAllPackageInfoTask(): TaskProvider<Task> =
        tasks.register("createAllPackageInfo") {
            group = "build"
            description = "Creates package-info.java files in all subprojects.s"
            dependsOn(subprojects.map { it.tasks.named("createPackageInfo") })
        }

}