package com.qtech.bubbles.util;

import com.qtech.bubbles.registry.object.ObjectHolder;
import org.jetbrains.annotations.Nullable;

public class RegistryUtils {
    @Nullable
    public static ObjectHolder getObjectHolder(Class<?> clazz) {
        return clazz.getDeclaredAnnotation(ObjectHolder.class);
    }
}
