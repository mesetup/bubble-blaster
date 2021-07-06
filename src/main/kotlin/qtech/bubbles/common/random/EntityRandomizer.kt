package qtech.bubbles.common.random

import qtech.bubbles.common.EntityProperties
import qtech.bubbles.common.gametype.AbstractGameMode
import java.awt.geom.Rectangle2D

/**
 * Abstract Entity Randomizer.
 */
abstract class EntityRandomizer {
    abstract fun getRandomProperties(bounds: Rectangle2D, gameMode: AbstractGameMode): EntityProperties
}