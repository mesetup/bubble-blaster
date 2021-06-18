package com.qtech.bubbles.common

import java.util.*

@Suppress("DEPRECATION")
@Deprecated("Used in kotlin", replaceWith = ReplaceWith("Pair(first, second)"))
class Pair<F, S>(var first: F, var second: S) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val pair = other as Pair<*, *>
        return first == pair.first && second == pair.second
    }

    override fun hashCode(): Int {
        return Objects.hash(first, second)
    }

    override fun toString(): String {
        return "($first, $second)"
    }
}