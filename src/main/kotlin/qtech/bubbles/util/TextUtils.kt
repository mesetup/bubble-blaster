package qtech.bubbles.util

object TextUtils {
    fun getRepresentationString(s: String): String {
        return s.replace("\\\\".toRegex(), "\\\\")
            .replace("\"".toRegex(), "\\\"")
            .replace("\b".toRegex(), "\\b")
            .replace("\n".toRegex(), "\\n")
            .replace("\r".toRegex(), "\\r")
            .replace("\t".toRegex(), "\\t")
            .replace("\u0000".toRegex(), "\\u0000")
            .replace("\u007f".toRegex(), "\\u007f")
    }
}