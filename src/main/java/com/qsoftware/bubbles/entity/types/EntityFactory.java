package com.qsoftware.bubbles.entity.types;

import com.qsoftware.bubbles.common.entity.Entity;
import com.qsoftware.bubbles.common.gametype.AbstractGameType;
import com.qsoftware.bubbles.common.scene.Scene;

public interface EntityFactory<T extends Entity> {
    T create(Scene scene, AbstractGameType gameType);
}
