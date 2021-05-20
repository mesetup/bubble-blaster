package com.qtech.bubbleblaster.common.screen;

import com.qtech.bubbleblaster.common.RegistryEntry;
import com.qtech.bubbleblaster.common.scene.Scene;

import java.util.Objects;
import java.util.function.Function;

@Deprecated
public class ScreenType<T extends Screen> extends RegistryEntry {
    // Fields
    private final Function<Scene, T> screenFactory;

    public ScreenType(Function<Scene, T> screenFactory) {
        // EffectInstance class field.
        this.screenFactory = screenFactory;
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
        return screenFactory.apply(scene);
    }
}
