package com.qtech.bubbles.event

class ExitEvent @JvmOverloads constructor(val reason: Reason? = null, val message: String? = null) : Event() {

    constructor(message: String?) : this(null, message) {}

    enum class Reason(val code: Int) {
        USER_EXIT(0), FATAL_ERROR(1);

    }
}