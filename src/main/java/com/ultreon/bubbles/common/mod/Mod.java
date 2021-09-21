package com.ultreon.bubbles.common.mod;

import com.ultreon.bubbles.event.bus.EventManagers;
import com.ultreon.hydro.event.bus.AbstractEvents;
import com.ultreon.hydro.event.bus.GameEvents;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.function.Supplier;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Mod {
    String modId();

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @interface EventBusSubscriber {
        String modId() default "";

        Bus bus() default Bus.BUBBLE_BLASTER;

        enum Bus {
            /**
             * The main Forge Event Bus.
             *
             * @see GameEvents#get()
             */
            BUBBLE_BLASTER(),

            /**
             * The mod specific Event bus.
             *
             * @see EventManagers#getModEvents(String)
             */
            MOD();

            /**
             * @return supplier to the event manager.
             * @deprecated should be used in another way in the future
             */
            @Deprecated
            public Supplier<AbstractEvents> bus() {
                return () -> null;
            }
        }
    }
}
