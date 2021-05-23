package com.qtech.bubbleblaster.entity.types;

import com.qtech.bubbleblaster.common.entity.Entity;
import com.qtech.bubbleblaster.common.gametype.AbstractGameType;

public interface EntityFactory<T extends Entity> {
    T create(AbstractGameType gameType);
}
