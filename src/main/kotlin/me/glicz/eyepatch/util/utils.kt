package me.glicz.eyepatch.util

import org.gradle.api.Project
import org.gradle.api.file.FileSystemLocation
import java.nio.file.Path
import java.nio.file.attribute.DosFileAttributeView
import kotlin.io.path.*

val FileSystemLocation.asPath: Path
    get() = asFile.toPath()

val Project.projectPath: Path
    get() = projectDir.toPath()

val Project.rootPath: Path
    get() = rootDir.toPath()

@OptIn(ExperimentalPathApi::class)
fun Path.forceDeleteRecursively() {
    walk(PathWalkOption.INCLUDE_DIRECTORIES).sortedByDescending { it }.forEach {
        it.fileAttributesViewOrNull<DosFileAttributeView>()?.setReadOnly(false)

        it.deleteExisting()
    }
}
