package me.glicz.eyepatch.util

import org.gradle.api.Project
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.io.PrintStream
import java.nio.file.Path

class Git(private val path: Path) {
    constructor(project: Project) : this(project.projectPath)

    operator fun invoke(vararg args: String): GitCommand = GitCommand(path, args)
}

class GitCommand internal constructor(
    private val path: Path,
    private val args: Array<out String>
) {
    fun run(silent: Boolean = false, silentErr: Boolean = false) {
        val process = ProcessBuilder("git", *args)
            .directory(path.toRealPath().toFile())
            .start()

        if (!silent) {
            redirect(process.inputStream, System.out)
        }
        if (!silentErr) {
            redirect(process.errorStream, System.err)
        }

        val exitCode = process.waitFor()
        if (exitCode != 0) {
            throw RuntimeException("git process ended with $exitCode exit code.")
        }
    }

    fun runSilently() = run(silent = true, silentErr = true)
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
