package com.qtech.test.bubbles

import qtech.hydro.Animation

object TestAnimation {
    @JvmStatic
    fun main(args: Array<String>) {
        val animation = Animation(100.0, 200.0, 30.0)
        animation.start()
        while (true) {
            animation.animate()
            try {
                Thread.sleep(1000)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }
}