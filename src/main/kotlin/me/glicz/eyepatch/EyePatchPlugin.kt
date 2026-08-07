package me.glicz.eyepatch

import io.codechicken.diffpatch.util.PatchMode
import me.glicz.eyepatch.task.ApplyPatches
import me.glicz.eyepatch.task.InitializeRepository
import me.glicz.eyepatch.task.MakePatches
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.internal.extensions.stdlib.capitalized
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.register

class EyePatchPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val eyePatchExt = project.extensions.create("eyepatch", EyePatchExtension::class.java)

        val initializeAllRepositories = project.tasks.register("initializeAllRepositories") {
            group = "eyepatch"
        }
        val applyAllPatches = project.tasks.register("applyAllPatches") {
            group = "eyepatch"
        }

        eyePatchExt.repositories.all details@{
            val capitalizedName = name.capitalized()

            val initializeRepository = project.tasks.register<InitializeRepository>(
                "initialize${capitalizedName}Repository"
            ) {
                group = "eyepatch"

                submodule = this@details.submodule
                repositoryDir = this@details.target
            }
            initializeAllRepositories.configure {
                dependsOn(initializeRepository)
            }

            val applyPatches = project.tasks.register<ApplyPatches>(
                "apply${capitalizedName}Patches"
            ) {
                group = "eyepatch"

                patchesDir = this@details.patches
                targetDir = initializeRepository.flatMap { it.repositoryDir }

                ignoredPrefixes = this@details.ignoredPrefixes
            }
            applyAllPatches.configure {
                dependsOn(applyPatches)
            }

            project.tasks.register<ApplyPatches>(
                "apply${capitalizedName}PatchesFuzzy"
            ) {
                group = "eyepatch"

                patchesDir = this@details.patches
                targetDir = initializeRepository.flatMap { it.repositoryDir }

                patchMode = PatchMode.FUZZY
                ignoredPrefixes = this@details.ignoredPrefixes
            }

            project.tasks.register<MakePatches>(
                "make${capitalizedName}Patches"
            ) {
                group = "eyepatch"

                submodule = this@details.submodule
                ignoredPrefixes = this@details.ignoredPrefixes

                repositoryDir = this@details.target
                patchesDir = this@details.patches

                finalizedBy(applyPatches)
            }
        }
    }
}