package com.qtech.bubbles.common.random

import com.qtech.bubbles.common.EntityProperties
import com.qtech.bubbles.common.gametype.AbstractGameMode
import java.awt.geom.Rectangle2D

/**
 * Abstract Entity Randomizer.
 */
abstract class EntityRandomizer {
    abstract fun getRandomProperties(bounds: Rectangle2D, gameMode: AbstractGameMode): EntityProperties
}