package qtech.bubbles.init

import qtech.bubbles.BBInternalMod
import qtech.bubbles.common.gametype.AbstractGameMode
import qtech.bubbles.common.init.ObjectInit
import qtech.bubbles.entity.AmmoEntity
import qtech.bubbles.entity.BubbleEntity
import qtech.bubbles.entity.GiantBubbleEntity
import qtech.bubbles.entity.player.PlayerEntity
import qtech.bubbles.entity.types.EntityType
import qtech.bubbles.registry.DeferredRegister
import qtech.bubbles.registry.Registers
import qtech.bubbles.registry.`object`.RegistryObject
import java.util.function.Supplier

//@ObjectHolder(addonId = "qbubbles")
object Entities : ObjectInit<EntityType<*>?> {
    val ENTITIES: DeferredRegister<EntityType<*>> = DeferredRegister.create(BBInternalMod.ADDON_ID, Registers.ENTITIES)
    val AMMO: RegistryObject<EntityType<AmmoEntity>> = register("bubble") { EntityType { gameMode: AbstractGameMode -> AmmoEntity(gameMode) } }
    val BUBBLE: RegistryObject<EntityType<BubbleEntity>> = register("bubble") { EntityType { gameMode: AbstractGameMode -> BubbleEntity(gameMode) } }
    val GIANT_BUBBLE: RegistryObject<EntityType<GiantBubbleEntity>> = register("giant_bubble") { EntityType { gameMode: AbstractGameMode -> GiantBubbleEntity(gameMode) } }
    val PLAYER: RegistryObject<EntityType<PlayerEntity>> = register("entity") { EntityType { gameType -> PlayerEntity(gameType) } }
    private fun <T : EntityType<*>> register(name: String, supplier: Supplier<T>): RegistryObject<T> {
        return ENTITIES.register(name, supplier)
    }
}