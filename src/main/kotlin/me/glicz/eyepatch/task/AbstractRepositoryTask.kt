package me.glicz.eyepatch.task

import me.glicz.eyepatch.util.Git
import me.glicz.eyepatch.util.JGit
import me.glicz.eyepatch.util.asPath
import me.glicz.eyepatch.util.forceDeleteRecursively
import org.eclipse.jgit.submodule.SubmoduleWalk
import org.eclipse.jgit.treewalk.filter.PathFilterGroup
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputDirectory
import kotlin.io.path.createDirectories
import kotlin.io.path.exists
import kotlin.io.path.writeText

abstract class AbstractRepositoryTask : DefaultTask() {
    @get:Input
    abstract val submodule: Property<String>

    @get:OutputDirectory
    abstract val repositoryDir: DirectoryProperty

    protected fun initSubmodule() {
        val git = Git(project)
        git("submodule", "update", "--init", "--recursive", "--force", submodule.get()).runSilently()
    }

    protected fun cloneRepository() {
        val remoteUrl: String
        val commitId: String

        JGit(project).use { jgit ->
            SubmoduleWalk.forIndex(jgit.repository).use { walk ->
                val submoduleName = submodule.get()
                walk.setFilter(PathFilterGroup.createFromStrings(submoduleName))

                require(walk.next())

                remoteUrl = walk.remoteUrl
                commitId = walk.objectId.name
            }
        }

        val repositoryDir = this@AbstractRepositoryTask.repositoryDir.get().asPath
        val git = Git(repositoryDir)

        try {
            require(repositoryDir.exists())

            git("reset", "--hard", commitId).runSilently()
        } catch (_: Exception) {
            repositoryDir.apply {
                forceDeleteRecursively()
                createDirectories()
            }

            git("clone", remoteUrl, ".").runSilently()
            git("checkout", commitId).runSilently()
            git("remote", "remove", "origin").runSilently()
        }

        git("submodule", "update", "--init", "--recursive", "--force").runSilently()

        val hooksDir = repositoryDir.resolve(".git/hooks").apply {
            createDirectories()
        }
        hooksDir.resolve("pre-commit").apply {
            writeText(
                """
                    #!/bin/sh
                    echo "Direct commits to this repository are not allowed."
                    echo "Please use the designated 'make patches' task instead."
                    exit 1
                """.trimIndent()
            )

            // could use NIO, but it's the easiest cross-platform way to do that
            toFile().setExecutable(true)
        }
    }
}
