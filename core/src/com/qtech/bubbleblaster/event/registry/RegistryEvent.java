package com.qtech.bubbleblaster.event.registry;

import com.qtech.bubbleblaster.common.IRegistryEntry;
import com.qtech.bubbleblaster.event.Event;
import com.qtech.bubbleblaster.registry.Registry;

import java.util.Objects;

public class RegistryEvent {
    public static class Register<T extends IRegistryEntry> extends Event {
        private final Registry<T> registry;

        public Register(Registry<T> registry) {
            super();
            this.registry = registry;
        }

        public Registry<T> getRegistry() {
            return registry;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Register<?> register = (Register<?>) o;
            return Objects.equals(getRegistry().getType(), register.getRegistry().getType());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getRegistry().getType());
        }
    }

    public static class Dump extends Event {
        public Dump() {
            super();
        }
    }
}
