package com.qsoftware.bubbles.common.addon;

import com.qsoftware.bubbles.addon.loader.AddonContainer;
import com.qsoftware.bubbles.addon.loader.AddonManager;
import com.qsoftware.bubbles.common.interfaces.NamespaceHolder;
import com.qsoftware.bubbles.event.bus.LocalAddonEventBus;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class AddonObject<T extends QBubblesAddon> implements NamespaceHolder {
    private final Class<T> addonClass;
    private final Addon annotation;
    private String namespace;
    private T instance;
    private final LocalAddonEventBus<T> eventBus;
    private final AddonContainer container;

    public AddonObject(String namespace, AddonContainer container, Addon annotation, Class<T> clazz) {
        this.annotation = annotation;
        this.setNamespace(namespace);
        this.addonClass = clazz;
        this.eventBus = new LocalAddonEventBus<T>(this);
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

    public Addon getAnnotation() {
        return annotation;
    }

    @NotNull
    public LocalAddonEventBus<T> getEventBus() {
        return eventBus;
    }

    @SuppressWarnings("unchecked")
    public T getAddon() {
        QBubblesAddon container = AddonManager.getInstance().getAddon(namespace);
        return (T) container;
    }

    public AddonContainer getContainer() {
        return container;
    }
}
