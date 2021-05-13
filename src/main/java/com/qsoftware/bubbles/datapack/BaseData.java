package com.qsoftware.bubbles.datapack;

import com.google.gson.Gson;
import com.qsoftware.bubbles.common.ResourceLocation;

public abstract class BaseData {
    private DataProperties properties;

    public BaseData(DataProperties properties) {
        this.properties = properties;
    }

    public Gson getGson(ResourceLocation resource) {
//        resource.openStream();
        return null;
    }
}
