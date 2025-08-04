package me.glicz.eyepatch.task

import org.gradle.api.tasks.TaskAction

abstract class InitializeRepository : AbstractRepositoryTask() {
    @TaskAction
    fun run() {
        initSubmodule()
        cloneRepository()
    }
}