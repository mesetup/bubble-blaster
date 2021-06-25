package com.qtech.bubbles.event.old;

@Deprecated
@SuppressWarnings({"unused"})
public abstract class QThreadedEvent<E> implements Runnable {
    //    protected static Map<Integer, Scene> sceneHandlers = Collections.synchronizedMap(new HashMap<>());
//    protected static Map<Integer, EventHandler<?>> handlers = Collections.synchronizedMap(new HashMap<>());
//    protected static Map<Integer, Object> eventInstances = Collections.synchronizedMap(new HashMap<>());
//    protected static Map<RenderEventPriority, CopyOnWriteArraySet<Integer>> priorityToHashCode = Collections.synchronizedMap(new HashMap<>());
//    protected static Map<Integer, CopyOnWriteArraySet<RenderEventPriority>> hashCodeToPriority = Collections.synchronizedMap(new HashMap<>());
//    private final int hash;
//    private Thread thread;
//    // Fields
//    private boolean eventActive = true;
    private boolean running;

//    // Constructor
//    protected QThreadedEvent(int hash) {
//        super();
//        this.hash = hash;
//    }
//
//    // Handlers
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
//    @SuppressWarnings("unchecked")
//    public static <E> void removeListener(int code) throws NoSuchElementException {
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
//    @Override
//    public int hashCode() {
//        return hash;
//    }
//
//    public synchronized void start() {
//        thread = new Thread(this);
//        thread.start();
//
//        running = true;
//    }
//
//    public synchronized final void call(E instance, Scene scene) {
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
//                String c = eventInstances.get(hashCode).getClass().getName();
//                String d = instance.getClass().getName();
////                System.out.println("(a==b) :: (" + a + " == " + b + ") = " + (a == b));
//                System.out.println("(c==d) :: (" + c + " == " + d + ") = " + (c == d));
//                System.out.println("(a==b && c==d) = " + (a == b && c == d));
//                if (a == b && c.equals(d)) {
//                    EventHandler<E> qEventEventHandler = (EventHandler<E>) handlers.get(hashCode);
//                    if (qEventEventHandler != null) {
//                        qEventEventHandler.run(instance);
//                    }
//                }
//            }
//        }
//    }

    // Thread
    @Override
    public void run() {
//        super.run();
        while (this.running) {
//            try {
//                System.out.println(getClass().getMethod("run").toString());
//            } catch (NoSuchMethodException e) {
//                e.printStackTrace();
//            }
            this.doEvent();
        }
    }

    // Do something
    @SuppressWarnings("EmptyMethod")
    protected abstract void doEvent();

//    // Deactivate
//    public void deactivateThread() {
//        this.eventActive = false;
//    }

}
