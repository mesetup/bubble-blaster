package com.qtech.bubbles.util

class ExceptionUtils private constructor() {
    companion object {
        fun utilityClass(): IllegalAccessError {
            return IllegalAccessError("Tried to initialize utility class.")
        }
    }

    init {
        throw utilityClass()
    }
}