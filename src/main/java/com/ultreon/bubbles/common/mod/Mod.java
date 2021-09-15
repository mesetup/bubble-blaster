package com.ultreon.bubbles.common.mod;

import com.ultreon.hydro.event.bus.EventBus;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.function.Supplier;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Mod {
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
             * @see com.ultreon.bubbles.event.bus.Bus#getQBubblesEventBus()
             */
            QBUBBLES(com.ultreon.bubbles.event.bus.Bus::getQBubblesEventBus),
            /**
             * The addon specific Event bus.
             *
             * @see com.ultreon.bubbles.event.bus.Bus#getAddonEventBus()
             */
            MOD(com.ultreon.bubbles.event.bus.Bus::getAddonEventBus);

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
