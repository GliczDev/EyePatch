package me.glicz.eyepatch.task

import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.UntrackedTask

@UntrackedTask(because = "Initializes repository")
abstract class InitializeRepository : AbstractRepositoryTask() {
    @TaskAction
    fun run() {
        initSubmodule()
        cloneRepository()
    }
}