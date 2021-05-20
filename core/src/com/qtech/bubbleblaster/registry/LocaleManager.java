package com.qtech.bubbleblaster.registry;

import com.qtech.bubbleblaster.common.maps.SequencedHashMap;
import com.qtech.bubbleblaster.common.text.translation.LanguageMap;

import java.util.*;

public final class LocaleManager extends AbstractRegistry<Locale, LanguageMap> {
    //    protected final static HashBasedTable<Object, Object, Object> registries = HashBasedTable.create();
    protected final static SequencedHashMap<Locale, LanguageMap> registry = new SequencedHashMap<>();

    public static LocaleManager getManager() {
        return new LocaleManager();
    }

    protected LocaleManager() throws IllegalStateException {

    }

    /**
     * Returns the registered instance from the given {@link Locale}
     *
     * @param obj the namespaced key.
     * @return an registered instance of the type {@link LanguageMap}.
     * @throws ClassCastException if the type is invalid.
     */
    public LanguageMap get(Locale obj) {
        return registry.get(obj);
    }

    /**
     * Register an object.
     *
     * @param key the register item key.
     * @param val the register item value.
     */
    @Override
    @Deprecated
    public void register(Locale key, LanguageMap val) {
        registry.put(key, val);
    }

    public void register(String locale) {
        Locale locale1 = Locale.forLanguageTag(locale);
        LanguageMap languageMap = new LanguageMap(locale1);
        register(locale1, languageMap);
    }

    @Override
    public Collection<LanguageMap> values() {
        return new ArrayList<>(registry.values());
    }

    @Override
    public Set<Locale> keys() {
        return registry.keySet();
    }

    @Override
    public Set<Map.Entry<Locale, LanguageMap>> entries() throws IllegalAccessException {
        // I do this because IDE won's accept dynamic values ans keys.
        ArrayList<LanguageMap> values = new ArrayList<>(values());
        ArrayList<Locale> keys = new ArrayList<>(keys());

        if (keys.size() != values.size()) throw new IllegalAccessException("Keys and values have different lengths.");

        Set<Map.Entry<Locale, LanguageMap>> entrySet = new HashSet<>();

        for (int i = 0; i < keys.size(); i++) {
            entrySet.add(new AbstractMap.SimpleEntry<>(keys.get(i), values.get(i)));
        }

        return entrySet;
    }
}
