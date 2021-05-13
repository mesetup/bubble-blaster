package com.qsoftware.bubbles.common.random;

import com.qsoftware.bubbles.common.EntityProperties;
import com.qsoftware.bubbles.common.gametype.AbstractGameType;

import java.awt.geom.Rectangle2D;

/**
 * Abstract Entity Randomizer.
 */
public abstract class EntityRandomizer {
    public abstract EntityProperties getRandomProperties(Rectangle2D bounds, AbstractGameType gameType);
}
