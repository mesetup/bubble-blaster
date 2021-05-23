package com.qtech.bubbleblaster.event.old;

import com.qtech.bubbleblaster.event.Event;
import com.qtech.bubbleblaster.event.RenderEventPriority;
import com.qtech.bubbleblaster.event.SubscribeEvent;
import com.qtech.bubbleblaster.event.bus.EventBus;

/**
 * <h1>Event baseclass.</h1>
 * The base class for internal events in this game, such as {@link QKeyboardEvent} or {@link QRenderEvent}
 *
 * @see RenderEventPriority
 * @deprecated use {@link SubscribeEvent}, {@link Event} and an subclass of {@link EventBus} instead.
 */
@Deprecated
public abstract class QEvent<E extends QEvent<?>> {
//    protected static Map<Integer, Scene> sceneHandlers = Collections.synchronizedMap(new HashMap<>());
//    protected static Map<Integer, EventHandler<?>> handlers = Collections.synchronizedMap(new HashMap<>());
//    protected static Map<Integer, Object> eventInstances = Collections.synchronizedMap(new HashMap<>());
//    protected static Map<RenderEventPriority, CopyOnWriteArraySet<Integer>> priorityToHashCode = Collections.synchronizedMap(new HashMap<>());
//    protected static Map<Integer, CopyOnWriteArraySet<RenderEventPriority>> hashCodeToPriority = Collections.synchronizedMap(new HashMap<>());
//    @Deprecated
//    protected static CopyOnWriteArraySet<CopyOnWriteArrayList<Object>> toAdd = new CopyOnWriteArraySet<>();
//    @Deprecated
//    protected static CopyOnWriteArraySet<CopyOnWriteArrayList<Object>> toRemove = new CopyOnWriteArraySet<>();
//    private final int hash;
//    public Scene scene;
//
//    protected QEvent(int hash) {
//        this.hash = hash;
//    }
//
//    public static <E> int addListener(E instance, Scene scene, EventHandler<E> handler, RenderEventPriority priority) {
//        int code = handler.hashCode();
////        QEvent.toAdd.add(new CopyOnWriteArrayList<>(new Object[]{code, priority, handler}));
//
//        if (!priorityToHashCode.containsKey(priority)) priorityToHashCode.put(priority, new CopyOnWriteArraySet<>());
//        if (!hashCodeToPriority.containsKey(code)) hashCodeToPriority.put(code, new CopyOnWriteArraySet<>());
//
//        priorityToHashCode.get(priority).add(code);
//        hashCodeToPriority.get(code).add(priority);
//        eventInstances.put(code, instance);
//        handlers.put(code, handler);
//        sceneHandlers.put(code, scene);
//
//        return code;
//    }
//
//    public static <E extends QEvent<?>> void removeListener(int code) throws NoSuchElementException {
////        toRemove.add(new CopyOnWriteArrayList<>(new Object[]{code}));
//
////        System.out.println("00_A: " + code);
////        System.out.println("00_D: " + handlers.containsKey(code));
//        int errors = 1;
//
//        try {
//            EventHandler<E> handler = (EventHandler<E>) handlers.get(code);
////        System.out.println("00_E: " + handlers.containsValue(handler));
//            handlers.remove(code, handler);
//        } catch (NullPointerException exc) {
////            throw new NoSuchElementException("Listener already removed or not created (yet).");
//            errors += 1;
//        }
//
//        try {
//            Scene sceneHandler = sceneHandlers.get(code);
////        System.out.println("00_E: " + handlers.containsValue(handler));
//            sceneHandlers.remove(code, sceneHandler);
//        } catch (NullPointerException exc) {
////            throw new NoSuchElementException("Listener already removed or not created (yet).");
//            errors += 1;
//        }
//
//        try {
//            QEvent<?> eventInstance = (QEvent<?>) eventInstances.get(code);
////        System.out.println("00_E: " + handlers.containsValue(handler));
//            eventInstances.remove(code, eventInstance);
//        } catch (NullPointerException exc) {
////            throw new NoSuchElementException("Listener already removed or not created (yet).");
//            errors += 1;
//        }
//
//        CopyOnWriteArraySet<RenderEventPriority> priorities = hashCodeToPriority.get(code);
//        if (priorities != null) {
//            try {
//                for (@Nullable RenderEventPriority priority : priorities) {
//                    try {
//                        priorityToHashCode.get(priority).remove(code);
//                    } catch (NullPointerException ignore) {
//                    }
//                }
//                try {
//                    hashCodeToPriority.remove(code, priorities);
//                } catch (NullPointerException exc) {
////                    throw new NoSuchElementException("Listener already partly removed. (hashCodeToPriority)");
//                    errors += 1;
//                }
//            } catch (NullPointerException exc) {
////                throw new NoSuchElementException("Listener already partly removed. (priorities)");
//                errors += 1;
//            }
//        } else {
//            hashCodeToPriority.remove(code);
////            Main.getLogger().warning("Priorities already cleaned up.");
//        }
//
//        if (errors == 5) {
//            throw new NoSuchElementException("Listener already removed or not created (yet).");
//        }
//    }
//
////    @SuppressWarnings("SuspiciousMethodCalls")
////    @Deprecated
////    protected void remove(Integer a) {
////        System.out.println("00_A: " + a);
////        System.out.println("00_D: " + handlers.containsKey(a));
////        EventHandler<E> c = (EventHandler<E>) handlers.get(a);
////
////        System.out.println("00_E: " + handlers.containsValue(c));
////        handlers.remove(a, c);
////
////        CopyOnWriteArraySet<RenderEventPriority> priorities = hashCodeToPriority.get(a);
////        for (RenderEventPriority priority : priorities) {
////            priorityToHashCode.get(priority).remove(a);
////        }
////        hashCodeToPriority.remove(a, priorities);
////
////        System.out.println("01_A:" + a);
////        System.out.println("01_D:" + handlers.containsKey(a));
////        System.out.println("01_E:" + handlers.containsValue(c));
////
////        toRemove.remove(a);
////    }
////
////    @Deprecated
////    protected void add(Integer a, RenderEventPriority b, EventHandler<E> c) {
////        if (!priorityToHashCode.containsKey(b)) priorityToHashCode.put(b, new CopyOnWriteArraySet<>());
////        if (!hashCodeToPriority.containsKey(a)) hashCodeToPriority.put(a, new CopyOnWriteArraySet<>());
////
////        priorityToHashCode.get(b).add(a);
////        hashCodeToPriority.get(a).add(b);
////        handlers.put(a, c);
////    }
////
////    @Deprecated
////    protected void tickHandlers() {
////        CopyOnWriteArraySet<CopyOnWriteArrayList<Object>> objectsAddClone = toAdd;
////
////        for (CopyOnWriteArrayList<Object> objects : objectsAddClone) {
////            Object a = objects.get(0);
////            Object b = objects.get(1);
////            Object c = objects.get(2);
////
//////            handlers.put((Integer)a, (Long)b, (EventHandler<QEvent>)c);
////            //noinspection unchecked
////            add((int)a, (RenderEventPriority)b, (EventHandler<E>)c);
////        }
////        toAdd.clear();
////
////        CopyOnWriteArraySet<CopyOnWriteArrayList<Object>> objectsRemoveClone = toRemove;
////        for (CopyOnWriteArrayList<Object> objects : objectsRemoveClone) {
////            Object a = objects.get(0);
////
////            System.out.println(a);
////
////            if (a instanceof Integer) {
////                remove((int) a);
////            }
////            objects.remove(0);
////            toRemove.remove(objects);
////        }
////        toRemove.clear();
////    }
//
//    @Override
//    public int hashCode() {
//        return hash;
//    }
//
//    public synchronized final void call(E instance, Scene scene) {
//        this.scene = scene;
//
//        if (scene != SceneManager.getInstance().getCurrentScene()) return;
//
//        RenderEventPriority[] eventPriorities = new RenderEventPriority[]{
//                /*RenderEventPriority.LOWEST, RenderEventPriority.LOWER, RenderEventPriority.LOW,*/
//                RenderEventPriority.NORMAL,
//                /*RenderEventPriority.HIGH, RenderEventPriority.HIGHER, RenderEventPriority.HIGHEST*/};
//
//        for (RenderEventPriority priority : eventPriorities) {
//            CopyOnWriteArraySet<Integer> hashCodes = priorityToHashCode.get(priority);
//
//            if (hashCodes == null) {
//                continue;
//            }
//            for (Integer hashCode : hashCodes) {
//                if (sceneHandlers.get(hashCode) == null) continue;
////                System.out.println(hashCode);
//
//                ResourceLocation a = sceneHandlers.get(hashCode).getRegistryName();
//                ResourceLocation b = SceneManager.getInstance().getCurrentScene().getRegistryName();
//                Object c = eventInstances.get(hashCode).getClass().getName();
//                Object d = instance.getClass().getName();
////                System.out.println("(a==b) :: (" + a + " == " + b + ") = " + (a == b));
////                System.out.println("(c==d) :: (" + c + " == " + d + ") = " + (c == d));
////                System.out.println("(a==b && c==d) = " + (a == b && c == d));
//                if (a == b && c == d) {
//                    EventHandler<E> qEventEventHandler = (EventHandler<E>) handlers.get(hashCode);
//                    if (qEventEventHandler != null) {
//                        qEventEventHandler.run(instance);
//                    }
//                }
//            }
//        }
////        System.out.println(handlers.keySet());
////        System.out.println(handlers.keySet().toArray().length == handlers.values().toArray().length);
//
////        tickHandlers();
//    }
}
