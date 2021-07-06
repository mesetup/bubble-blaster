package qtech.bubbles.common.runnables

fun interface Applier<T, R> {
    fun apply(obj: T): R
}