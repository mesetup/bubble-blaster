package com.qtech.bubbles.common

import java.io.Serializable

class Percentage : Serializable, Comparable<Percentage> {
    val percentage: Double

    constructor(percentage: Double) {
        this.percentage = percentage * 100
    }

    constructor(value: Float) {
        percentage = value.toDouble()
    }

    val value: Double
        get() = percentage / 100

    override fun compareTo(other: Percentage): Int {
        return java.lang.Double.compare(percentage, other.percentage)
    }
}