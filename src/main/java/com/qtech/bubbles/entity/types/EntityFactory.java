package com.qtech.bubbles.entity.types;

import com.qtech.bubbles.common.entity.Entity;
import com.qtech.bubbles.common.gametype.AbstractGameType;
import com.qtech.bubbles.common.scene.Scene;
import com.qtech.bubbles.common.screen.Screen;

public interface EntityFactory<T extends Entity> {
    T create(AbstractGameType gameType);
}
