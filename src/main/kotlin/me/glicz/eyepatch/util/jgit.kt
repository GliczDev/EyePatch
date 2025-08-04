@file:Suppress("FunctionName")

package me.glicz.eyepatch.util

import org.eclipse.jgit.api.Git
import org.gradle.api.Project
import java.nio.file.Path

fun JGit(project: Project) = JGit(project.projectPath)

fun JGit(path: Path): Git = Git.open(path.toFile())
