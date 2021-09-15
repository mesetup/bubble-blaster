package com.ultreon.bubbles.entity.types;

import com.ultreon.bubbles.common.gametype.AbstractGameType;
import com.ultreon.bubbles.entity.Entity;

public interface EntityFactory<T extends Entity> {
    T create(AbstractGameType gameType);
}
