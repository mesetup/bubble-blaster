package com.qtech.bubbles.registry;

import com.qtech.bubbles.common.IRegistryEntry;
import com.qtech.bubbles.common.ResourceEntry;
import com.qtech.bubbles.common.init.ObjectInit;
import com.qtech.bubbles.common.maps.SequencedHashMap;
import com.qtech.bubbles.event._common.SubscribeEvent;
import com.qtech.bubbles.event.bus.Bus;
import com.qtech.bubbles.event.bus.EventBus;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.*;


@SuppressWarnings("unchecked")
public class Registry<T extends IRegistryEntry> {
    private final SequencedHashMap<ResourceEntry, T> registry = new SequencedHashMap<>();
    private final Class<T> type;
    private static final HashMap<Class<?>, Registry<?>> metricsMap = new HashMap<>();
    private final ResourceEntry registryName;

    protected Registry(@NotNull Class<T> clazz, ResourceEntry registryName) throws IllegalStateException {
        if (metricsMap.containsKey(clazz)) {
            throw new IllegalStateException();
        }

        this.registryName = registryName;
        this.type = clazz;
        Bus.getQBubblesEventBus().register(this);

        Registry.metricsMap.put(type, this);
    }

    public ResourceEntry getRegistryName() {
        return registryName;
    }

    @Deprecated
    public static <T extends RegistryEntry> Registry<T> create(@NotNull Class<T> clazz, ResourceEntry registryName) {
        return new Registry<>(clazz, registryName);
    }

    public static <T extends RegistryEntry> Registry<T> getRegistry(Class<T> objType) {
        return (Registry<T>) metricsMap.get(objType);
    }

    /**
     * Returns the registered instance from the given {@link ResourceEntry}
     *
     * @param key the namespaced key.
     * @return an registered instance of the type {@link T}.
     * @throws ClassCastException if the type is invalid.
     */
    public T get(ResourceEntry key) {
        if (!registry.containsKey(key)) {
            throw new IllegalArgumentException("Cannot find object for: " + key + " | type: " + type.getSimpleName());
        }
        return registry.get(key);
    }

    public boolean contains(ResourceEntry rl) {
        return registry.containsKey(rl);
    }

    @SubscribeEvent
    public void onRegistryDump() {
        System.out.println("Registry dump: " + type.getSimpleName());
//        System.out.println(hashCode());
        for (Map.Entry<ResourceEntry, T> entry : entries()) {
            T object = entry.getValue();
            ResourceEntry rl = entry.getKey();

            System.out.println("  (" + rl + ") -> " + object);
        }
    }

    public void register(Class<? extends ObjectInit<T>> clazz, String addonId) {
        // Get fields.
        Field[] fields = clazz.getDeclaredFields();

        System.out.println("Registering object-initialization class: " + clazz);

        // Loop fields.
        for (Field field : fields) {
            // Try.
            try {
                if (type.isAssignableFrom(field.getType())) {
                    // Get register-object.
                    T object = (T) field.get(null);

                    // Set key.
                    if (object.getRegistryName() == null) {
                        if (object.isTempRegistryName()) {
                            object.updateRegistryName(addonId);
                        } else {
                            object.setRegistryName(new ResourceEntry(addonId, field.getName().toLowerCase()));
                        }
                    }

                    // Register if it isn't.
                    if (!values().contains(object)) {
                        register(object.getRegistryName(), object);
                    }
                }
            } catch (IllegalAccessException e) {
                // Oops, some problem occurred.
                e.printStackTrace();
            }
        }
    }

    /**
     * Register an object.
     *
     * @param rl  the resource location.
     * @param val the register item value.
     */
    public void register(ResourceEntry rl, T val) {
        if (!type.isAssignableFrom(val.getClass())) {
            throw new IllegalArgumentException("Not allowed type detected, got " + val.getClass() + " expected assignable to " + type);
        }

        registry.put(rl, val);
    }

    public Collection<T> values() {
        return Collections.unmodifiableCollection(registry.values());
    }

    public Set<ResourceEntry> keys() {
        return Collections.unmodifiableSet(registry.keySet());
    }

    public Set<Map.Entry<ResourceEntry, T>> entries() {
        // I do this because IDE won's accept dynamic values ans keys.
        ArrayList<T> values = new ArrayList<>(values());
        ArrayList<ResourceEntry> keys = new ArrayList<>(keys());

        if (keys.size() != values.size()) throw new IllegalStateException("Keys and values have different lengths.");

        Set<Map.Entry<ResourceEntry, T>> entrySet = new HashSet<>();

        for (int i = 0; i < keys.size(); i++) {
            entrySet.add(new AbstractMap.SimpleEntry<>(keys.get(i), values.get(i)));
        }

        return Collections.unmodifiableSet(entrySet);
    }

    public Class<T> getType() {
        return type;
    }

    @Nullable
    @Deprecated
    public EventBus.Handler getHandler() {
        return null;
    }

    public void registrable(ResourceEntry rl, RegistryEntry object) {
        if (type.isAssignableFrom(object.getClass())) {
            register(rl, (T) object);
        }
    }
}
