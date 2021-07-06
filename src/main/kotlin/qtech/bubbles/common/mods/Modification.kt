package qtech.bubbles.common.mods

import qtech.bubbles.event.bus.EventBus
import java.util.function.Supplier

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.CLASS)
annotation class Modification(val namespace: String) {
    @Retention(AnnotationRetention.RUNTIME)
    @Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.CLASS)
    annotation class EventBusSubscriber(val addonId: String = "", val bus: Bus = Bus.QBUBBLES) {
        enum class Bus(private val busSupplier: Supplier<EventBus>) {
            /**
             * The main Forge Event Bus.
             *
             * @see qtech.bubbles.event.Bus.getQBubblesEventBus
             */
            QBUBBLES(Supplier<EventBus> { qtech.bubbles.event.Bus.qBubblesEventBus }),

            /**
             * The addon specific Event bus.
             *
             * @see qtech.bubbles.event.Bus.getModEventBus
             */
            MOD(Supplier<EventBus> { qtech.bubbles.event.Bus.addonEventBus });

            fun bus(): Supplier<EventBus> {
                return busSupplier
            }
        }
    }
}