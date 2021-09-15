package com.ultreon.hydro.common;

import java.util.Objects;

@SuppressWarnings("ConstantConditions")
public abstract class RegistryEntry implements IRegistryEntry {
    private ResourceEntry registryName = null;

    public void setRegistryName(String namespace, String name) {
        this.registryName = new ResourceEntry(namespace, name);
    }

    public boolean isTempRegistryName() {
        return this.registryName.namespace() == null;
    }

    public void updateRegistryName(String namespace) {
        if (this.registryName.namespace() == null) {
            this.registryName = this.registryName.withNamespace(namespace);
        }
    }

    public ResourceEntry getRegistryName() {
        return registryName;
    }

    public void setRegistryName(String name) {
        this.registryName = new ResourceEntry(null, name);
    }

    public void setRegistryName(ResourceEntry name) {
        this.registryName = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegistryEntry that = (RegistryEntry) o;
        return Objects.equals(this.registryName, that.registryName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.registryName);
    }
}
