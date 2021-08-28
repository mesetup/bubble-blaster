package com.qtech.bubbles.common;

import com.qtech.bubbles.core.exceptions.IllegalCharacterException;

/**
 * Namespace, used for holding and checking namespaces.
 *
 * @author Quinten
 * @since 1.0.0
 * @deprecated this object is useless now.
 */
@Deprecated
public class Namespace {
    private String namespace;

    public Namespace(String namespace) throws IllegalCharacterException {
        @SuppressWarnings("SpellCheckingInspection") String firstAllowed = "abcdefghijklmnopqrstuvwxyz";
        @SuppressWarnings("SpellCheckingInspection") String allowed = "abcdefghijklmnopqrstuvwxyz_0123456789";
        @SuppressWarnings("SpellCheckingInspection") String lastAllowed = "abcdefghijklmnopqrstuvwxyz0123456789";

        char[] chars = namespace.toCharArray();

        // Check name containment.
        if (!firstAllowed.contains(String.valueOf(chars[0]))) {
            throw new IllegalCharacterException("Namespace starts with invalid char: ");
        }

        if (!lastAllowed.contains(String.valueOf(chars[chars.length - 1]))) {
            throw new IllegalCharacterException("Namespace ends with invalid char: ");
        }

        for (int i = 1; i < chars.length - 1; i++) {
            if (!allowed.contains(String.valueOf(chars[i]))) {
                throw new IllegalCharacterException("Namespace contains invalid char: " + chars[i]);
            }

            i++;
        }

        // Assign namespace field.
        this.namespace = namespace;
    }

    public String getNamespace() {
        return namespace;
    }

    protected void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    @Override
    public String toString() {
        return namespace;
    }
}
