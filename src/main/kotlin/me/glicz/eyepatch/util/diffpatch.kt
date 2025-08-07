package me.glicz.eyepatch.util

import io.codechicken.diffpatch.cli.DiffOperation
import io.codechicken.diffpatch.cli.PatchOperation
import io.codechicken.diffpatch.util.Input
import io.codechicken.diffpatch.util.Output
import java.nio.file.Path

fun PatchOperation.Builder.baseInput(path: Path) = baseInput(Input.SingleInput.path(path))

fun PatchOperation.Builder.patchesInput(path: Path) = patchesInput(Input.SingleInput.path(path))

fun PatchOperation.Builder.patchedOutput(path: Path) = patchedOutput(Output.SingleOutput.path(path))

fun DiffOperation.Builder.baseInput(path: Path) = baseInput(Input.SingleInput.path(path))

fun DiffOperation.Builder.changedInput(path: Path) = changedInput(Input.SingleInput.path(path))

fun DiffOperation.Builder.patchesOutput(path: Path) = patchesOutput(Output.SingleOutput.path(path))
