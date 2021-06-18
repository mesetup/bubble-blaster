package com.qtech.bubbles.core.exceptions

class SceneParameterCountError(needed: Int, got: Int) : RuntimeException(getMessage(needed, got)) {
    companion object {
        private fun getMessage(needed: Int, got: Int): String {
            val a: String = if (needed == 1 || needed == -1) {
                "parameter"
            } else {
                "parameters"
            }
            return "Needed $needed $a got $got."
        }
    }
}