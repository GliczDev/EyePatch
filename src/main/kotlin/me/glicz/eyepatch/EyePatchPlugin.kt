package me.glicz.eyepatch

import me.glicz.eyepatch.task.ApplyPatches
import me.glicz.eyepatch.task.InitializeRepository
import me.glicz.eyepatch.task.MakePatches
import me.glicz.eyepatch.util.asPath
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.TaskProvider
import org.gradle.internal.extensions.stdlib.capitalized
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.invoke
import org.gradle.kotlin.dsl.register
import kotlin.io.path.createDirectories

class EyePatchPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val eyePatchExt = project.extensions.create("eyepatch", EyePatchExtension::class.java)

        val initializeRepositoryTasks = mutableListOf<TaskProvider<InitializeRepository>>()
        val applyPatchesTasks = mutableListOf<TaskProvider<ApplyPatches>>()

        eyePatchExt.repositories.all details@{
            val capitalizedName = name.capitalized()

            project.tasks {
                val initializeRepository = register<InitializeRepository>("initialize${capitalizedName}Repository") {
                    group = "eyepatch"

                    submodule = this@details.submodule
                    repositoryDir = this@details.target
                }
                initializeRepositoryTasks += initializeRepository

                val applyPatches = register<ApplyPatches>("apply${capitalizedName}Patches") {
                    group = "eyepatch"

                    patchesDir = this@details.patches.map { it -> it.asPath.createDirectories(); it }
                    targetDir = initializeRepository.flatMap { it.repositoryDir }
                    ignoredPrefixes = this@details.ignoredPrefixes
                }
                applyPatchesTasks += applyPatches

                register<MakePatches>("make${capitalizedName}Patches") {
                    group = "eyepatch"

                    submodule = this@details.submodule
                    ignoredPrefixes = this@details.ignoredPrefixes

                    repositoryDir = this@details.target
                    patchesDir = this@details.patches

                    finalizedBy(applyPatches)
                }
            }
        }

        project.afterEvaluate {
            project.tasks.register("initializeAllRepositories") {
                group = "eyepatch"

                dependsOn(initializeRepositoryTasks)
            }

            project.tasks.register("applyAllPatches") {
                group = "eyepatch"

                dependsOn(applyPatchesTasks)
            }
        }
    }
}