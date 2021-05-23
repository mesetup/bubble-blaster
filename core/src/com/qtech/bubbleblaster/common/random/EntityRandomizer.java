package com.qtech.bubbleblaster.common.random;

import com.qtech.bubbleblaster.common.EntityProperties;
import com.qtech.bubbleblaster.common.gametype.AbstractGameType;


/**
 * Abstract Entity Randomizer.
 */
public abstract class EntityRandomizer {
    public abstract EntityProperties getRandomProperties(Rectangle2D bounds, AbstractGameType gameType);
}
