package com.qtech.bubbles.common.bubble

class Range @JvmOverloads constructor(val start: Double, val end: Double, val step: Double = 1.0) {

    constructor(end: Double) : this(0.0, end) {}

    operator fun contains(value: Double): Boolean {
        return start <= value && end > value
    }

    operator fun iterator(): MutableIterator<Double?> {
        return object : MutableIterator<Double?> {
            private var current = start
            override fun hasNext(): Boolean {
                return current < end
            }

            override fun next(): Double {
                return step.let { current += it; current }
            }

            override fun remove() {
                throw IllegalArgumentException("Remove not allowed")
            }
        }
    }

    fun iterable(): Iterable<Double?> {
        return Iterable { this@Range.iterator() }
    }
}