package com.qtech.bubbles.common.addon

import com.qtech.bubbles.event.bus.EventBus
import java.util.function.Supplier

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.CLASS)
annotation class Addon(val namespace: String) {
    @Retention(AnnotationRetention.RUNTIME)
    @Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.CLASS)
    annotation class EventBusSubscriber(val addonId: String = "", val bus: Bus = Bus.QBUBBLES) {
        enum class Bus(private val busSupplier: Supplier<EventBus>) {
            /**
             * The main Forge Event Bus.
             *
             * @see com.qtech.bubbles.event.Bus.getQBubblesEventBus
             */
            QBUBBLES(Supplier<EventBus> { com.qtech.bubbles.event.Bus.qBubblesEventBus }),

            /**
             * The addon specific Event bus.
             *
             * @see com.qtech.bubbles.event.Bus.getAddonEventBus
             */
            MOD(Supplier<EventBus> { com.qtech.bubbles.event.Bus.addonEventBus });

            fun bus(): Supplier<EventBus> {
                return busSupplier
            }
        }
    }
}