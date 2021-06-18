package com.qtech.bubbles.entity.types

import com.qtech.bubbles.common.RegistryEntry
import com.qtech.bubbles.entity.Entity
import com.qtech.bubbles.common.gametype.AbstractGameMode
import net.querz.nbt.tag.CompoundTag
import java.util.*

open class EntityType<T : Entity>(private val entityFactory: EntityFactory<T>) : RegistryEntry() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as EntityType<*>
        return registryName == that.registryName
    }

    fun create(gameMode: AbstractGameMode): T {
        return entityFactory.create(gameMode)
    }

    override fun hashCode(): Int {
        return Objects.hash(registryName)
    }

    fun create(gameMode: AbstractGameMode, nbt: CompoundTag): T {
        val t = entityFactory.create(gameMode)
        t.setState(nbt)
        return t
    }
}