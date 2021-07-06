package qtech.bubbles.common.runnables

fun interface DoubleParameterizedRunnable<A, B> {
    fun run(a: A, b: B)
}