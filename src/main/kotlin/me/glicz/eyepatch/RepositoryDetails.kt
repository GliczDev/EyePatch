package me.glicz.eyepatch

import org.gradle.api.file.ProjectLayout
import org.gradle.api.model.ObjectFactory
import org.gradle.kotlin.dsl.property
import org.gradle.kotlin.dsl.setProperty

abstract class RepositoryDetails(val name: String, objects: ObjectFactory, layout: ProjectLayout) {
    val submodule = objects.property<String>()

    val target = objects.directoryProperty().convention(
        layout.projectDirectory.dir(name)
    )

    val patches = objects.directoryProperty().convention(
        layout.projectDirectory.dir("patches/$name")
    )

    val ignoredPrefixes = objects.setProperty<String>()
}