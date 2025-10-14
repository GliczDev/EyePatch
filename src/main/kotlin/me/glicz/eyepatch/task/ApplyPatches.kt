package me.glicz.eyepatch.task

import io.codechicken.diffpatch.cli.PatchOperation
import io.codechicken.diffpatch.util.PatchMode
import me.glicz.eyepatch.util.*
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.SetProperty
import org.gradle.api.tasks.*
import kotlin.io.path.createDirectories

@UntrackedTask(because = "Patches repository")
abstract class ApplyPatches : DefaultTask() {
    @get:InputDirectory
    abstract val patchesDir: DirectoryProperty

    @get:InputDirectory
    abstract val targetDir: DirectoryProperty

    @get:Input
    @get:Optional
    abstract val ignoredPrefixes: SetProperty<String>

    @TaskAction
    fun run() {
        val patchesDir = patchesDir.get().asPath.apply {
            createDirectories()
        }
        val targetDir = targetDir.get().asPath

        val result = PatchOperation.builder().run {
            baseInput(targetDir)
            patchesInput(patchesDir)
            patchedOutput(targetDir)
            mode(PatchMode.OFFSET)
            lineEnding("\n")
            ignorePrefix(".git")

            ignoredPrefixes.orNull?.forEach(::ignorePrefix)

            build().operate()
        }

        result.summary?.print(System.out, false)

        JGit(targetDir).use { git ->
            git.commit().apply {
                setAll(true)
                setAllowEmpty(true)
                setCommitter("eyepatch", "eyepatch@eyepatch.xyz")
                setMessage("file patches")
                setSign(false)
                call()
            }
        }
    }
}