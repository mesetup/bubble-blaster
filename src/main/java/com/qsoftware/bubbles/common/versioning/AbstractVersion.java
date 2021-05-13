package com.qsoftware.bubbles.common.versioning;

public abstract class AbstractVersion<T extends AbstractVersion<T>> implements Comparable<T> {
    public abstract boolean isStable();

    public abstract String toString();
}
