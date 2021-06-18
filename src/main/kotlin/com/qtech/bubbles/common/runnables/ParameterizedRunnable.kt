package com.qtech.bubbles.common.runnables

fun interface ParameterizedRunnable<T> {
    fun run(t: T)
}