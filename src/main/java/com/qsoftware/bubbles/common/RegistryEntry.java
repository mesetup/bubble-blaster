package com.qsoftware.bubbles.common;

import java.util.Objects;

public abstract class RegistryEntry implements IRegistryEntry {
    private ResourceLocation registryName = null;

    public void setRegistryName(String namespace, String name) {
        registryName = new ResourceLocation(namespace, name);
    }

    public boolean isTempRegistryName() {
        return registryName.getNamespace() == null;
    }

    public void updateRegistryName(String namespace) {
        if (registryName.getNamespace() == null) {
            registryName = registryName.withNamespace(namespace);
        }
    }

    public ResourceLocation getRegistryName() {
        return registryName;
    }

    public void setRegistryName(String name) {
        registryName = new ResourceLocation(null, name);
    }

    public void setRegistryName(ResourceLocation name) {
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
