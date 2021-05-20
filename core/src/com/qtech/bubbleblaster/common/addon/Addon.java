package com.qtech.bubbleblaster.common.addon;

import com.qtech.bubbleblaster.event.bus.EventBus;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.function.Supplier;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Addon {
    String addonId();

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @interface EventBusSubscriber {
        String addonId() default "";

        Bus bus() default Bus.QBUBBLES;

        enum Bus {
            /**
             * The main Forge Event Bus.
             *
             * @see com.qtech.bubbleblaster.event.Bus#getQBubblesEventBus()
             */
            QBUBBLES(com.qtech.bubbleblaster.event.Bus::getQBubblesEventBus),
            /**
             * The addon specific Event bus.
             *
             * @see com.qtech.bubbleblaster.event.Bus#getAddonEventBus()
             */
            MOD(com.qtech.bubbleblaster.event.Bus::getAddonEventBus);

            private final Supplier<EventBus> busSupplier;

            Bus(final Supplier<EventBus> eventBusSupplier) {
                this.busSupplier = eventBusSupplier;
            }

            public Supplier<EventBus> bus() {
                return busSupplier;
            }
        }
    }
}
