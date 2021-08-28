package com.qtech.bubbles.entity.types;

import com.qtech.bubbles.common.gametype.AbstractGameType;
import com.qtech.bubbles.entity.Entity;

public interface EntityFactory<T extends Entity> {
    T create(AbstractGameType gameType);
}
