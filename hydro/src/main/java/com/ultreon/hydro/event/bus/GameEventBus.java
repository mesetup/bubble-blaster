package com.ultreon.hydro.event.bus;

import com.ultreon.commons.lang.Pair;
import com.ultreon.hydro.event.Event;
import com.ultreon.hydro.event._common.ICancellable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.Consumer;
import java.util.function.Predicate;

@SuppressWarnings({"unused", "unchecked", "DuplicatedCode"})
public class GameEventBus extends EventBus {
    private static final Logger LOGGER = LogManager.getLogger("Game-Events");
    private static final GameEventBus instance = new GameEventBus();
    public List<Handler> handlers = new ArrayList<>();
    public Map<Long, Class<? extends Event>> classMap = new HashMap<>();
    public final Map<Class<? extends Event>, CopyOnWriteArraySet<Pair<Object, Method>>> eventToMethod = new HashMap<>();

    public final HashMap<Pair<Object, Method>, CopyOnWriteArraySet<Class<? extends Event>>> methodToEvent = new HashMap<>();

    public static GameEventBus get() {
        return instance;
    }

    @Override
    public <T extends Event> boolean post(T event) {
        if (!eventToMethod.containsKey(event.getClass())) {
            return false;
        }

        CopyOnWriteArraySet<Pair<Object, Method>> methods = eventToMethod.get(event.getClass());
        for (Pair<Object, Method> method : methods) {
            try {
                method.getSecond().invoke(method.getFirst(), event);
            } catch (InvocationTargetException e) {
                LOGGER.error("Cannot invoke event handler error follows:");
                e.getCause().printStackTrace();
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }

        return event instanceof ICancellable && ((ICancellable) event).isCancelled();
    }

    @Override
    public void register(Class<?> clazz) {
        loopDeclaredMethods(clazz, (method) -> {
            // Get types and values.
            Class<? extends Event> event = (Class<? extends Event>) method.getParameterTypes()[0];
            addHandlers(event, null, method);
        });
    }

    @Override
    public void register(Object o) {
        loopMethods(o, (method) -> {
            // Get types and values.
            Class<? extends Event> event = (Class<? extends Event>) method.getParameterTypes()[0];
            addHandlers(event, o, method);
        });
    }

    @Override
    public void unregister(Class<? extends Event> event, Class<?> clazz) {
        loopDeclaredMethods(clazz, (method) -> {
            // Get and check event.
            Class<? extends Event> evt = (Class<? extends Event>) method.getParameterTypes()[0];
            if (event == evt) {
                // Remove handler.
                try {
                    removeHandlers(event, null, method);
                } catch (IllegalStateException ignored) {

                }
            }
        });
    }

    @Override
    public void unregister(Class<? extends Event> event, Object o) {
        loopMethods(o, (method) -> {
            // Get types and values.
            Class<? extends Event> evt = (Class<? extends Event>) method.getParameterTypes()[0];
            if (event == evt) {
                // Remove handler.
                try {
                    removeHandlers(event, o, method);
                } catch (IllegalStateException ignored) {

                }
            }
        });
    }

    @Override
    public void unregister(Class<?> clazz) {
        loopDeclaredMethods(clazz, (method) -> {
            // Get and check event.
            Class<? extends Event> evt = (Class<? extends Event>) method.getParameterTypes()[0];

            // Remove handler.
            try {
                removeHandlers(evt, null, method);
            } catch (IllegalStateException ignored) {

            }
        });
    }

    @Override
    public void unregister(Object o) {
        loopMethods(o, (method) -> {
            // Get types and values.
            Class<? extends Event> evt = (Class<? extends Event>) method.getParameterTypes()[0];

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
            if (instancePredicate.test(method)) {
                // Set accessible.
                method.setAccessible(true);
                consumer.accept(method);
            }
        }
    }

    private void removeHandlers(Class<? extends Event> event, @Nullable Object obj, Method method) {
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

    protected void addHandlers(Class<? extends Event> event, @Nullable Object obj, Method method) {
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

//    public <T extends Event> Handler register(Class<T> clazz, EventHandler<T> eventHandler) {
//        return register(clazz, eventHandler, EventPriority.NORMAL);
//    }
//
//    @SuppressWarnings("unchecked")
//    public <T extends Event> Handler register(Class<T> clazz, EventHandler<T> eventHandler, EventPriority priority) {
//        long id = System.nanoTime();
//        Handler handler = new Handler() {
//            private final SequencedHashMap<Class<? extends Event>, ArrayList<EventHandler<Event>>> handlers = new SequencedHashMap<>();
//
//            {
//                handlers.put(clazz, new ArrayList<>());
//                handlers.get(clazz).add(new EventHandler<>() {
//                    @Override
//                    public void handle(Event event1) {
////                        System.out.println(method);
////                        System.out.println(o);
////                        System.out.println(event1);
//
////                        System.out.println(event);
////                        System.out.println(event1);
//                        eventHandler.handle((T) event1);
//                    }
//
//                    @Override
//                    public EventPriority getPriority() {
//                        return priority;
//                    }
//
//                    @Override
//                    public SubscribeEvent getAnnotation() {
//                        return null;
//                    }
//
//                    @Override
//                    public Class<? extends Event> getType() {
//                        return clazz;
//                    }
//                });
//            }
//
//            @Override
//            public void onRemove() {
//
//            }
//
//            @SuppressWarnings("unchecked")
//            @Override
//            public <U extends Event> Collection<EventHandler<U>> getHandlers(Class<U> clazz) {
//                Collection<EventHandler<U>> handlers1 = new ArrayList<>();
//
//                if (!handlers.containsKey(clazz)) {
//                    return handlers1;
//                }
//
//                for (EventHandler<? extends Event> handler : handlers.get(clazz)) {
//                    if (handler.getType().isAssignableFrom(clazz)) {
//                        handlers1.add((EventHandler<U>) handler);
//                    }
//                }
//                return handlers1;
//            }
//
//            @Override
//            public long id() {
//                return id;
//            }
//        };
//
//        handlers.add(handler);
//
//        return handler;
//    }
}
