package com.qtech.bubbles.common.runnables

@Deprecated("")
fun interface EventHandler<T> {
    fun run(evt: T)
}