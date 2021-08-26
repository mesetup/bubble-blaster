package com.qtech.bubbles.common;

import com.qtech.bubbles.core.utils.categories.StringUtils;
import com.qtech.utilities.python.builtins.ValueError;

import java.io.Serializable;
import java.util.Objects;

/**
 * The {@link NamespacedKey} is used instead of IDs, for customizing purposes
 *
 * @author Quinten Jungblut
 */
@Deprecated
public record NamespacedKey(String namespace, String key) implements Serializable {
    /**
     * The {@link NamespacedKey} is used instead of IDs, for customizing purposes
     *
     * @param namespace The key. (Like a value in a {@link java.util.HashMap})
     * @param key       The namespace. (Like a key in a {@link java.util.HashMap})
     */
    public NamespacedKey {
    }

    public static NamespacedKey fromString(String s) {
        int colonCount = StringUtils.count(s, ':');

        if (colonCount == 1) {
            String[] splitted = s.split(":");
            String key = splitted[0];
            String namespace = splitted[1];
            return new NamespacedKey(key, namespace);

        } else if (colonCount < 1) {
            throw new ValueError("Too few colons. Needed 1, got 0");
        } else {
            throw new ValueError("Too much colons. Needed 1, got " + colonCount);
        }
    }

    public static NamespacedKey qbubbles(String key) {
        return new NamespacedKey("qbubbles", key);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NamespacedKey that = (NamespacedKey) o;
        return namespace.equals(that.namespace) &&
                key.equals(that.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(namespace, key);
    }

    @Override
    public String toString() {
        return namespace + ':' + key;
    }
}
