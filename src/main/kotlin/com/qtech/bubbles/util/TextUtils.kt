package com.qtech.bubbles.util

class TextUtils private constructor() {
    companion object {
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

    init {
        throw UnsupportedOperationException("Tried to initialize utility class")
    }
}