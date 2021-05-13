package com.qsoftware.bubbles.common;

import com.qsoftware.bubbles.core.exceptions.IllegalCharacterException;

public class Namespace {
    private String namespace;

    public Namespace(String namespace) throws IllegalCharacterException {
        @SuppressWarnings("SpellCheckingInspection") String firstAllowed = "abcdefghijklmnopqrstuvwxyz";
        @SuppressWarnings("SpellCheckingInspection") String allowed = "abcdefghijklmnopqrstuvwxyz_0123456789";
        @SuppressWarnings("SpellCheckingInspection") String lastAllowed = "abcdefghijklmnopqrstuvwxyz0123456789";

        char[] chars = namespace.toCharArray();

        // Check name containment.
        if (!firstAllowed.contains(new String(new char[]{chars[0]}))) {
            throw new IllegalCharacterException("Namespace starts with invalid char: ");
        }

        if (!lastAllowed.contains(new String(new char[]{chars[chars.length - 1]}))) {
            throw new IllegalCharacterException("Namespace ends with invalid char: ");
        }

        for (int i = 1; i < chars.length - 1; i++) {
            if (!allowed.contains(new String(new char[]{chars[i]}))) {
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
