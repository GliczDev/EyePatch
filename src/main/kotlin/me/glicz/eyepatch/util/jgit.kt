@file:Suppress("FunctionName")

package me.glicz.eyepatch.util

import org.eclipse.jgit.api.Git
import org.gradle.api.Project
import java.nio.file.Path

typealias JGit = Git

fun JGit(project: Project) = JGit(project.projectPath)

fun JGit(path: Path): JGit = JGit.open(path.toFile())
