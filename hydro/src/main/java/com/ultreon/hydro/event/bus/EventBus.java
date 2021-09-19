package com.ultreon.hydro.event.bus;

import com.ultreon.hydro.event.Event;
import com.ultreon.hydro.event.EventHandler;
import com.ultreon.hydro.event.SubscribeEvent;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.function.Predicate;

public abstract class EventBus {
    public abstract <T extends Event> boolean post(T event);

    protected static final Predicate<Method> classPredicate;
    protected static final Predicate<Method> instancePredicate;

    static {
        Predicate<Method> isHandler = EventBus::isEventHandler;
        Predicate<Method> isSubscriber = EventBus::isSubscriber;
        classPredicate = isHandler.and(isSubscriber).and((method) -> Modifier.isStatic(method.getModifiers()));
        instancePredicate = isHandler.and(isSubscriber).and((method) -> !Modifier.isStatic(method.getModifiers()));
    }

    private static boolean isSubscriber(Method method) {
        return method.isAnnotationPresent(SubscribeEvent.class);
    }

    private static boolean isEventHandler(Method method) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameterTypes.length == 1) {
            Class<?> clazz1 = parameterTypes[0];
            return Event.class.isAssignableFrom(clazz1);
        }
        return false;
    }

    public abstract void register(Class<?> clazz);

    public abstract void register(Object o);

    public abstract void unregister(Class<? extends Event> event, Class<?> clazz);

    public abstract void unregister(Class<? extends Event> event, Object o);

    public abstract void unregister(Class<?> clazz);

    public abstract void unregister(Object o);

    public static abstract class Handler {
        protected abstract void onRemove();

        public abstract <T extends Event> Collection<EventHandler<T>> getHandlers(Class<T> clazz);

        public abstract long id();

        public void unbind() {
            onRemove();
        }

        @SuppressWarnings("unchecked")
        <T extends Event> void handle(T event) {
            Collection<EventHandler<T>> handlers = getHandlers((Class<T>) event.getClass());
            if (handlers == null) {
                return;
            }

            for (EventHandler<T> handler : handlers) {
                handler.handle(event);
            }
        }
    }
}
