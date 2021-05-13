package com.qsoftware.bubbles.common.addon;

import com.qsoftware.bubbles.event.bus.EventBus;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.function.Supplier;

@SuppressWarnings("JavadocReference")
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Addon {
    String addonId();

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @interface EventBusSubscriber {
        /**
         * The addoId
         *
         * @return
         */
        String addonId() default "";

        Bus bus() default Bus.QBUBBLES;

        enum Bus {
            /**
             * The main Forge Event Bus.
             *
             * @see com.qsoftware.bubbles.event.Bus#getQBubblesEventBus()
             */
            QBUBBLES(com.qsoftware.bubbles.event.Bus::getQBubblesEventBus),
            /**
             * The addon specific Event bus.
             *
             * @see com.qsoftware.bubbles.event.Bus#getAddonEventBus()
             */
            MOD(com.qsoftware.bubbles.event.Bus::getAddonEventBus);

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
