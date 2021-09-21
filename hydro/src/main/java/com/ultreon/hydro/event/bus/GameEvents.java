package com.ultreon.hydro.event.bus;

import com.ultreon.commons.lang.Pair;
import com.ultreon.hydro.event.Event;
import com.ultreon.commons.lang.ICancellable;
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
public class GameEvents extends AbstractEvents<Event> {
    private static final GameEvents instance = new GameEvents(LogManager.getLogger("Game-Events"));
    public GameEvents(Logger logger) {
        super(logger);
    }

    public static GameEvents get() {
        return instance;
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
