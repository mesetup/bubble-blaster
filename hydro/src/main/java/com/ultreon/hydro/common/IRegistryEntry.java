package com.ultreon.hydro.common;

public interface IRegistryEntry {
    ResourceEntry getRegistryName();

    void setRegistryName(ResourceEntry rl);

    void updateRegistryName(String namespace);

    boolean isTempRegistryName();
}
