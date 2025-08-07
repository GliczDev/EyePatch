package me.glicz.eyepatch.util

import io.codechicken.diffpatch.cli.DiffOperation
import io.codechicken.diffpatch.cli.PatchOperation
import io.codechicken.diffpatch.util.Input
import io.codechicken.diffpatch.util.Output
import java.nio.file.Path

fun PatchOperation.Builder.baseInput(path: Path) = baseInput(Input.MultiInput.folder(path))

fun PatchOperation.Builder.patchesInput(path: Path) = patchesInput(Input.MultiInput.folder(path))

fun PatchOperation.Builder.patchedOutput(path: Path) = patchedOutput(Output.MultiOutput.folder(path))

fun DiffOperation.Builder.baseInput(path: Path) = baseInput(Input.MultiInput.folder(path))

fun DiffOperation.Builder.changedInput(path: Path) = changedInput(Input.MultiInput.folder(path))

fun DiffOperation.Builder.patchesOutput(path: Path) = patchesOutput(Output.MultiOutput.folder(path))
