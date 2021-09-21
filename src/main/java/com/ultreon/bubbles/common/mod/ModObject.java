package com.ultreon.bubbles.common.mod;

import com.ultreon.bubbles.common.interfaces.NamespaceHolder;
import com.ultreon.bubbles.event.bus.ModEvents;
import com.ultreon.bubbles.mod.ModContainer;
import com.ultreon.bubbles.mod.loader.ModManager;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class ModObject<T extends ModInstance> implements NamespaceHolder {
    private final Class<T> modClass;
    private final Mod annotation;
    private String namespace;
    private T instance;
    private final ModEvents<T> eventBus;
    private final ModContainer container;

    public ModObject(String namespace, ModContainer container, Mod annotation, Class<T> clazz) {
        this.annotation = annotation;
        this.setNamespace(namespace);
        this.modClass = clazz;
        this.eventBus = new ModEvents<>(this);
        this.container = container;
    }

    @Override
    public final String getNamespace() {
        return namespace;
    }

    @Override
    public final void setNamespace(String namespace) {
        if (this.namespace == null) {
            this.namespace = namespace;
        } else {
            throw new IllegalStateException("String can only set once.");
        }
    }

    public Class<T> getModClass() {
        return modClass;
    }

    public Mod getAnnotation() {
        return annotation;
    }

    @NotNull
    public ModEvents<T> getEventBus() {
        return eventBus;
    }

    @SuppressWarnings("unchecked")
    public T getMod() {
        ModInstance container = ModManager.instance().getMod(namespace);
        return (T) container;
    }

    public ModContainer getContainer() {
        return container;
    }
}
