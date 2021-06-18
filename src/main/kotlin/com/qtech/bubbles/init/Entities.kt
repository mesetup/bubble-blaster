package com.qtech.bubbles.init

import com.qtech.bubbles.BBInternalAddon
import com.qtech.bubbles.common.gametype.AbstractGameMode
import com.qtech.bubbles.common.init.ObjectInit
import com.qtech.bubbles.entity.AmmoEntity
import com.qtech.bubbles.entity.BubbleEntity
import com.qtech.bubbles.entity.GiantBubbleEntity
import com.qtech.bubbles.entity.player.PlayerEntity
import com.qtech.bubbles.entity.types.EntityType
import com.qtech.bubbles.registry.DeferredRegister
import com.qtech.bubbles.registry.Registers
import com.qtech.bubbles.registry.`object`.RegistryObject
import java.util.function.Supplier

//@ObjectHolder(addonId = "qbubbles")
object Entities : ObjectInit<EntityType<*>?> {
    val ENTITIES: DeferredRegister<EntityType<*>> = DeferredRegister.create(BBInternalAddon.ADDON_ID, Registers.ENTITIES)
    val AMMO: RegistryObject<EntityType<AmmoEntity>> = register("bubble") { EntityType { gameMode: AbstractGameMode -> AmmoEntity(gameMode) } }
    val BUBBLE: RegistryObject<EntityType<BubbleEntity>> = register("bubble") { EntityType { gameMode: AbstractGameMode -> BubbleEntity(gameMode) } }
    val GIANT_BUBBLE: RegistryObject<EntityType<GiantBubbleEntity>> = register("giant_bubble") { EntityType { gameMode: AbstractGameMode -> GiantBubbleEntity(gameMode) } }
    val PLAYER: RegistryObject<EntityType<PlayerEntity>> = register("entity") { EntityType { gameType -> PlayerEntity(gameType) } }
    private fun <T : EntityType<*>> register(name: String, supplier: Supplier<T>): RegistryObject<T> {
        return ENTITIES.register(name, supplier)
    }
}