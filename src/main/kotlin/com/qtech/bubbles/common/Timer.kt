package com.qtech.bubbles.common

object Timer {
    @kotlin.jvm.JvmStatic
    val time: Double
        get() = System.nanoTime().toDouble() / 1000000000.0
}