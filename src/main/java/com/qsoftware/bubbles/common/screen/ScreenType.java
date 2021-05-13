package com.qsoftware.bubbles.common.screen;

import com.qsoftware.bubbles.common.RegistryEntry;
import com.qsoftware.bubbles.common.scene.Scene;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

public class ScreenType<T extends Screen<T>> extends RegistryEntry {
//    private static final ArrayList<ScreenType<?>> SCREEN_TYPES = new ArrayList<>();

    // Types
//    public static final ScreenType<PauseScreen> PAUSE_SCREEN = new ScreenType<>(PauseScreen.class, "pause_screen");
//    public static final ScreenType<CommandScreen> COMMAND_SCREEN = new ScreenType<>(CommandScreen.class, "command_screen");

    // Fields
    private final Class<T> screenClass;

    public ScreenType(Class<T> screenClass) {

        // EffectInstance class field.
        this.screenClass = screenClass;

        // Add effectTypes
//        SCREEN_TYPES.add(this);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScreenType<?> that = (ScreenType<?>) o;
        return Objects.equals(getRegistryName(), that.getRegistryName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRegistryName());
    }

    public T getScreen(Scene scene) {

        // Constructor
        Constructor<T> constructor;
        try {
            constructor = screenClass.getConstructor(Scene.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }

        // Instance
        T instance;
        try {
            instance = constructor.newInstance(scene);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }

        return instance;
    }
}
