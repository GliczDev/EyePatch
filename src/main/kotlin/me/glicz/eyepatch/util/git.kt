package me.glicz.eyepatch.util

import org.gradle.api.Project
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.io.PrintStream
import java.nio.file.Path

private val DEBUG = System.getProperty("eyepatch.debug").toBoolean()

class Git(private val path: Path) {
    constructor(project: Project) : this(project.projectPath)

    operator fun invoke(vararg args: String): GitCommand = GitCommand(path, args)
}

class GitCommand internal constructor(
    private val path: Path,
    private val args: Array<out String>
) {
    fun run(silentOut: Boolean = false, silentErr: Boolean = false) {
        val process = ProcessBuilder("git", *args)
            .directory(path.toRealPath().toFile())
            .start()

        if (!silentOut || DEBUG) {
            redirect(process.inputStream, System.out)
        }
        if (!silentErr || DEBUG) {
            redirect(process.errorStream, System.err)
        }

        val exitCode = process.waitFor()
        if (exitCode != 0) {
            throw RuntimeException("git process ended with $exitCode exit code.")
        }
    }

    fun runSilently() = run(silentOut = true, silentErr = true)
}

private fun redirect(`is`: InputStream, out: PrintStream) {
    val thread = Thread {
        BufferedReader(InputStreamReader(`is`)).use { reader ->
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                out.println(line)
            }
        }
    }
    thread.isDaemon = true
    thread.start()
}
