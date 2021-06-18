package com.qtech.bubbles.common.interfaces

import com.qtech.bubbles.event.SubscribeEvent

fun interface EventConsumer<T> {
    @SubscribeEvent
    fun accept(evt: T)
}