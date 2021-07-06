package qtech.bubbles.entity.types

import qtech.bubbles.entity.Entity
import qtech.bubbles.common.gametype.AbstractGameMode

fun interface EntityFactory<T : Entity> {
    fun create(gameMode: AbstractGameMode): T
}