package me.glicz.eyepatch.task

import io.codechicken.diffpatch.cli.DiffOperation
import me.glicz.eyepatch.util.*
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.SetProperty
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import kotlin.io.path.createDirectories

abstract class MakePatches : AbstractRepositoryTask() {
    @get:Input
    @get:Optional
    abstract val ignoredPrefixes: SetProperty<String>

    @get:OutputDirectory
    abstract val patchesDir: DirectoryProperty

    @TaskAction
    fun run() {
        initSubmodule()

        val patchesDir = patchesDir.get().asPath.apply {
            forceDeleteRecursively()
            createDirectories()
        }
        val submodule = this@MakePatches.submodule.get()
        val cloneDir = repositoryDir.get().asPath

        val result = DiffOperation.builder().run {
            baseInput(project.projectPath.resolve(submodule))
            changedInput(cloneDir)
            patchesOutput(patchesDir)
            autoHeader(true)
            lineEnding("\n")
            ignorePrefix(".git")

            ignoredPrefixes.orNull?.forEach(::ignorePrefix)

            build().operate()
        }

        result.summary?.print(System.out, false)
    }
}