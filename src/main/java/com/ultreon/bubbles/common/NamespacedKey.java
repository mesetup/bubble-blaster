package com.ultreon.bubbles.common;

import com.ultreon.commons.util.StringUtils;
import com.ultreon.commons.utilities.python.builtins.ValueError;
import com.ultreon.hydro.common.ResourceEntry;

import java.io.Serializable;
import java.util.Objects;

/**
 * The {@link NamespacedKey} is used instead of IDs, for customizing purposes
 *
 * @author Qboi
 * @since 1.0.0
 * @deprecated replaced by {@link ResourceEntry}.
 */
@Deprecated(since = "1.0.0")
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

    public static NamespacedKey bubbleblaster(String key) {
        return new NamespacedKey("bubbleblaster", key);
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
