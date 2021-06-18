package com.qtech.bubbles.common.entity

import java.util.*

open class DamageSourceType protected constructor(private val name: String) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as DamageSourceType
        return name == that.name
    }

    override fun hashCode(): Int {
        return Objects.hash(name)
    }

    companion object {
        // Types
        @JvmField
        val COLLISION = DamageSourceType("collision")
        @JvmField
        val POISON = DamageSourceType("poison")
        val UNKNOWN = DamageSourceType("unknown")
        val GENERIC = DamageSourceType("generic")
    }
}