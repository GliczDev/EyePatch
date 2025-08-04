package me.glicz.eyepatch

import org.gradle.api.model.ObjectFactory
import org.gradle.kotlin.dsl.domainObjectContainer

abstract class EyePatchExtension(objects: ObjectFactory) {
    val repositories = objects.domainObjectContainer(RepositoryDetails::class)
}