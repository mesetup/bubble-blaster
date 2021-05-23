package com.qtech.bubbleblaster.common;

public interface IRegistryEntry {
    ResourceLocation getRegistryName();

    void setRegistryName(ResourceLocation rl);

    void updateRegistryName(String namespace);

    boolean isTempRegistryName();
}
