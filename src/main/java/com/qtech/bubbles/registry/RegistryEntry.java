package com.qtech.bubbles.registry;

import com.qtech.bubbles.common.IRegistryEntry;
import com.qtech.bubbles.common.ResourceEntry;

import java.util.Objects;

public abstract class RegistryEntry implements IRegistryEntry {
    private ResourceEntry registryName = null;

    public void setRegistryName(String namespace, String name) {
        registryName = new ResourceEntry(namespace, name);
    }

    public boolean isTempRegistryName() {
        return registryName.namespace() == null;
    }

    public void updateRegistryName(String namespace) {
        if (registryName.namespace() == null) {
            registryName = registryName.withNamespace(namespace);
        }
    }

    public ResourceEntry getRegistryName() {
        return registryName;
    }

    public void setRegistryName(String name) {
        registryName = new ResourceEntry(null, name);
    }

    public void setRegistryName(ResourceEntry name) {
        this.registryName = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegistryEntry that = (RegistryEntry) o;
        return Objects.equals(registryName, that.registryName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(registryName);
    }
}
