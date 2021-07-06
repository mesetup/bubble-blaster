package qtech.utilities.common

import kotlin.math.pow
import kotlin.math.roundToLong

object FileSize {
    fun toString(size: Long): String {
        if (size < 1024) {
            return "$size B"
        }
        if (size >= 1024 && size < 1024 * 1024) {
            return "${(size.toDouble() / 1024).roundToLong()} KB"
        }
        if (size >= 1024 * 1024 && size < 1024 * 1024 * 1024) {
            return "${(size.toDouble() / 1024 / 1024).roundToLong()} MB"
        }
        if (size >= 1024 * 1024 * 1024 && size < 1024L * 1024 * 1024 * 1024) {
            return "${(size.toDouble() / 1024 / 1024 / 1024).roundToLong()} GB"
        }
        if (size >= 1024L * 1024 * 1024 * 1024 && size < 1024L * 1024 * 1024 * 1024 * 1024) {
            return "${(size.toDouble() / 1024 / 1024 / 1024 / 1024).roundToLong()} TB"
        }
        if (size >= 1024L * 1024 * 1024 * 1024 * 1024 && size < 1024L * 1024 * 1024 * 1024 * 1024 * 1024) {
            return "${(size.toDouble() / 1024 / 1024 / 1024 / 1024 / 1024).roundToLong()} PB"
        }
        if (size >= 1024L * 1024 * 1024 * 1024 * 1024 * 1024) {
            return "${(size.toDouble() / 1024 / 1024 / 1024 / 1024 / 1024 / 1024).roundToLong()} XB"
        }
        return "null@0"
    }
}