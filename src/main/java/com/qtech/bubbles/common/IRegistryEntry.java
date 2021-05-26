package com.qtech.bubbles.common;

public interface IRegistryEntry {
    ResourceLocation getRegistryName();

    void setRegistryName(ResourceLocation rl);

    void updateRegistryName(String namespace);

    boolean isTempRegistryName();
}
