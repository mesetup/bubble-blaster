package com.ultreon.bubbles.common.mod;

import com.ultreon.bubbles.mod.ModContainer;
import com.ultreon.bubbles.mod.loader.ModManager;
import com.ultreon.bubbles.common.interfaces.NamespaceHolder;
import com.ultreon.bubbles.event.bus.LocalAddonEventBus;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class ModObject<T extends ModInstance> implements NamespaceHolder {
    private final Class<T> addonClass;
    private final Mod annotation;
    private String namespace;
    private T instance;
    private final LocalAddonEventBus<T> eventBus;
    private final ModContainer container;

    public ModObject(String namespace, ModContainer container, Mod annotation, Class<T> clazz) {
        this.annotation = annotation;
        this.setNamespace(namespace);
        this.addonClass = clazz;
        this.eventBus = new LocalAddonEventBus<>(this);
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

    public Class<T> getAddonClass() {
        return addonClass;
    }

    public Mod getAnnotation() {
        return annotation;
    }

    @NotNull
    public LocalAddonEventBus<T> getEventBus() {
        return eventBus;
    }

    @SuppressWarnings("unchecked")
    public T getAddon() {
        ModInstance container = ModManager.getInstance().getAddon(namespace);
        return (T) container;
    }

    public ModContainer getContainer() {
        return container;
    }
}
