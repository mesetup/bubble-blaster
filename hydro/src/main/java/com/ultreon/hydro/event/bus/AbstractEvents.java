package com.ultreon.hydro.event.bus;

import com.ultreon.commons.lang.ICancellable;
import com.ultreon.commons.lang.Pair;
import com.ultreon.hydro.event.AbstractEvent;
import com.ultreon.hydro.event.SubscribeEvent;
import com.ultreon.hydro.event.Subscriber;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.Consumer;
import java.util.function.Predicate;

public abstract class AbstractEvents<T extends AbstractEvent> {
    protected static final Predicate<Method> classPredicate;

    protected static final Predicate<Method> instancePredicate;
    static {
        Predicate<Method> isHandler = AbstractEvents::isSubscriber;
        Predicate<Method> isSubscriber = AbstractEvents::isSubscribing;
        classPredicate = isHandler.and(isSubscriber).and((method) -> Modifier.isStatic(method.getModifiers()));
        instancePredicate = isHandler.and(isSubscriber).and((method) -> !Modifier.isStatic(method.getModifiers()));
    }

    public List<AbstractSubscription> subscriptions = new ArrayList<>();
    public Map<Long, Class<? extends AbstractEvent>> classMap = new HashMap<>();

    public final Map<Class<? extends AbstractEvent>, CopyOnWriteArraySet<Pair<Object, Method>>> eventToMethod = new HashMap<>();
    public final Map<Pair<Object, Method>, CopyOnWriteArraySet<Class<? extends AbstractEvent>>> methodToEvent = new HashMap<>();

    protected final Logger logger;

    public AbstractEvents(Logger logger) {
        this.logger = logger;
    }

    private static boolean isSubscribing(Method method) {
        return method.isAnnotationPresent(SubscribeEvent.class);
    }

    private static boolean isSubscriber(Method method) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameterTypes.length == 1) {
            Class<?> clazz1 = parameterTypes[0];
            return AbstractEvent.class.isAssignableFrom(clazz1);
        }
        return false;
    }

    public <E extends T> boolean publish(E event) {
        if (!eventToMethod.containsKey(event.getClass())) {
            return false;
        }

        CopyOnWriteArraySet<Pair<Object, Method>> methods = eventToMethod.get(event.getClass());
        for (Pair<Object, Method> method : methods) {
            try {
                method.getSecond().invoke(method.getFirst(), event);
            } catch (InvocationTargetException e) {
                logger.error("Cannot invoke event handler error follows:");
                e.getCause().printStackTrace();
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }

        return event instanceof ICancellable && ((ICancellable) event).isCancelled();
    }

    public void subscribe(Class<?> clazz) {
        loopDeclaredMethods(clazz, (method) -> {
            // Get types and values.
            Class<? extends AbstractEvent> event = (Class<? extends AbstractEvent>) method.getParameterTypes()[0];
            addHandlers(event, null, method);
        });
    }

    public void subscribe(Object o) {
        loopMethods(o, (method) -> {
            // Get types and values.
            Class<? extends AbstractEvent> event = (Class<? extends AbstractEvent>) method.getParameterTypes()[0];
            addHandlers(event, o, method);
        });
    }

    public void unsubscribe(Class<? extends T> event, Class<?> clazz) {
        loopDeclaredMethods(clazz, (method) -> {
            // Get and check event.
            Class<? extends AbstractEvent> evt = (Class<? extends AbstractEvent>) method.getParameterTypes()[0];
            if (event == evt) {
                // Remove handler.
                try {
                    removeHandlers(event, null, method);
                } catch (IllegalStateException ignored) {

                }
            }
        });
    }

    public void unsubscribe(Class<? extends T> event, Object o) {
        loopMethods(o, (method) -> {
            // Get types and values.
            Class<? extends AbstractEvent> evt = (Class<? extends AbstractEvent>) method.getParameterTypes()[0];
            if (event == evt) {
                // Remove handler.
                try {
                    removeHandlers(event, o, method);
                } catch (IllegalStateException ignored) {

                }
            }
        });
    }

    public void unsubscribe(Class<?> clazz) {
        loopDeclaredMethods(clazz, (method) -> {
            // Get and check event.
            Class<? extends AbstractEvent> evt = (Class<? extends AbstractEvent>) method.getParameterTypes()[0];

            // Remove handler.
            try {
                removeHandlers(evt, null, method);
            } catch (IllegalStateException ignored) {

            }
        });
    }

    public void unsubscribe(Object o) {
        loopMethods(o, (method) -> {
            // Get types and values.
            Class<? extends AbstractEvent> evt = (Class<? extends AbstractEvent>) method.getParameterTypes()[0];

            // Remove handler.
            try {
                removeHandlers(evt, o, method);
            } catch (IllegalStateException ignored) {

            }
        });
    }

    private void loopDeclaredMethods(Class<?> clazz, Consumer<Method> consumer) {
        // Loop declared methods.
        loopMethods0(clazz.getDeclaredMethods(), classPredicate, consumer);
    }

    private void loopMethods(Object o, Consumer<Method> consumer) {
        // Loop methods.
        loopMethods0(o.getClass().getMethods(), instancePredicate, consumer);
    }

    private void loopMethods0(Method[] methods, Predicate<Method> predicate, Consumer<Method> consumer) {
        // Check all methods for event subscribers.
        for (Method method : methods) {
            // Check is instance method.
            if (predicate.test(method)) {
                // Set accessible.
                method.setAccessible(true);
                consumer.accept(method);
            }
        }
    }

    private void removeHandlers(Class<? extends AbstractEvent> event, @Nullable Object obj, Method method) {
        Pair<Object, Method> pair = new Pair<>(obj, method);
        if (!eventToMethod.containsKey(event)) {
            throw new IllegalStateException("Cannot unregister method for a non-registered event.");
        } else if (!eventToMethod.get(event).contains(pair)) {
            throw new IllegalStateException("Cannot unregister an unregistered method.");
        }

        if (!methodToEvent.containsKey(pair)) {
            throw new IllegalStateException("Cannot unregister an unregistered method.");
        } else if (!methodToEvent.get(pair).contains(event)) {
            throw new IllegalStateException("Cannot unregister method for a non-registered event.");
        }

        methodToEvent.get(pair).remove(event);
        eventToMethod.get(event).remove(pair);
    }

    private void removeAllEvents(@Nullable Object obj, Method method) {
        Pair<Object, Method> pair = new Pair<>(obj, method);
        if (!methodToEvent.containsKey(pair)) {
            throw new IllegalStateException("Cannot unregister an unregistered method.");
        }

        for (Class<?> event : methodToEvent.get(pair)) {
            eventToMethod.get(event).remove(pair);
        }

        methodToEvent.remove(pair);
    }

    protected void addHandlers(Class<? extends AbstractEvent> event, @Nullable Object obj, Method method) {
        Pair<Object, Method> pair = new Pair<>(obj, method);
        if (!eventToMethod.containsKey(event)) {
            eventToMethod.put(event, new CopyOnWriteArraySet<>());
        }
        if (!methodToEvent.containsKey(pair)) {
            methodToEvent.put(pair, new CopyOnWriteArraySet<>());
        }
        eventToMethod.get(event).add(pair);
        methodToEvent.get(pair).add(event);
//        System.out.println(eventToMethod);
//        System.out.println(methodToEvent);
//        System.out.println(hashCode());
    }

    public static abstract class AbstractSubscription {
        protected abstract void onRemove();

        public abstract <T extends AbstractEvent> Collection<Subscriber<T>> getSubscribers(Class<T> clazz);

        public abstract long id();

        public void unsubscribe() {
            onRemove();
        }

        @SuppressWarnings("unchecked")
        <T extends AbstractEvent> void onPublish(T event) {
            Collection<Subscriber<T>> handlers = getSubscribers((Class<T>) event.getClass());
            if (handlers == null) {
                return;
            }

            for (Subscriber<T> handler : handlers) {
                handler.handle(event);
            }
        }
    }
}
