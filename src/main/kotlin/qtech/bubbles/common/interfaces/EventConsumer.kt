package qtech.bubbles.common.interfaces

import qtech.bubbles.event.SubscribeEvent

fun interface EventConsumer<T> {
    @SubscribeEvent
    fun accept(evt: T)
}