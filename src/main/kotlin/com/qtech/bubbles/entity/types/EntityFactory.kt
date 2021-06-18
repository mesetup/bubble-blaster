package com.qtech.bubbles.entity.types

import com.qtech.bubbles.entity.Entity
import com.qtech.bubbles.common.gametype.AbstractGameMode

fun interface EntityFactory<T : Entity> {
    fun create(gameMode: AbstractGameMode): T
}