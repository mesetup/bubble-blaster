package com.qtech.bubbles.common.random;

import com.qtech.bubbles.common.EntityProperties;
import com.qtech.bubbles.common.gametype.AbstractGameType;

import java.awt.geom.Rectangle2D;

/**
 * Abstract Entity Randomizer.
 */
public abstract class EntityRandomizer {
    public abstract EntityProperties getRandomProperties(Rectangle2D bounds, AbstractGameType gameType);
}
