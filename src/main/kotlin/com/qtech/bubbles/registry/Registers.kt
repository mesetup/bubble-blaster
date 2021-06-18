package com.qtech.bubbles.registry

import com.google.common.annotations.Beta
import com.qtech.bubbles.bubble.AbstractBubble
import com.qtech.bubbles.common.ResourceLocation
import com.qtech.bubbles.common.ability.AbilityType
import com.qtech.bubbles.common.ammo.AmmoType
import com.qtech.bubbles.common.cursor.RegistrableCursor
import com.qtech.bubbles.common.effect.Effect
import com.qtech.bubbles.common.gamestate.GameEvent
import com.qtech.bubbles.common.gametype.AbstractGameMode
import com.qtech.bubbles.entity.types.EntityType
import com.qtech.bubbles.graphics.TextureCollection
import com.qtech.bubbles.item.Item

/**
 * Registers, use this class to create deferred registers for specific things in the game.
 *
 * @see DeferredRegister
 */
object Registers {
    /**
     * Bubble register.
     * Bubbles are the core mechanic of the game, without the bubbles the game shouldn't exist.
     *
     * @see Bubbles
     */
    val BUBBLES: Registry<AbstractBubble> = Registry.create(AbstractBubble::class.java, ResourceLocation("bubbleblaster", "bubbles"))

    /**
     * Ammo type register.
     * Ammo types are what it says ammo types, you can create different ammo types doing different things.
     *
     * @see AmmoTypes
     */
    val AMMO_TYPES: Registry<AmmoType> = Registry.create(AmmoType::class.java, ResourceLocation("bubbleblaster", "ammo_types"))

    /**
     * Entity register.
     * Entities are one of the core things in a game, they are used for creating the player, enemies or in this case also bubbles.
     *
     * @see Entities
     */
    val ENTITIES: Registry<EntityType<*>> = Registry.create(EntityType::class.java, ResourceLocation("bubbleblaster", "entities"))

    /**
     * Effect register.
     * Effects are classes for doing things over time after activating, and can stop after some time.
     *
     * @see Effects
     */
    val EFFECTS: Registry<Effect> = Registry.create(Effect::class.java, ResourceLocation("bubbleblaster", "effects"))

    /**
     * Ability register.
     * Abilities are classes for doing things like teleportation.
     *
     * @see Abilities
     */
    val ABILITIES: Registry<AbilityType<*>> = Registry.create(AbilityType::class.java, ResourceLocation("bubbleblaster", "abilities"))

    /**
     * Game state register.
     *
     * @see GameEvents
     */
    val GAME_EVENTS: Registry<GameEvent> = Registry.create(GameEvent::class.java, ResourceLocation("bubbleblaster", "game_states"))

    /**
     * Game type register.
     *
     * @see GameTypes
     */
    val GAME_TYPES: Registry<AbstractGameMode> = Registry.create(AbstractGameMode::class.java, ResourceLocation("bubbleblaster", "game_types"))

    /**
     * Cursor register.
     */
    val CURSORS: Registry<RegistrableCursor> = Registry.create(RegistrableCursor::class.java, ResourceLocation("bubbleblaster", "cursors"))

    /**
     * Items register, will be used in the future.
     */
    @Beta
    val ITEMS: Registry<Item> = Registry.create(Item::class.java, ResourceLocation("bubbleblaster", "items"))

    /**
     * Texture collection register.
     * Used for mapping and preloading textures.
     *
     * @see TextureCollections
     */
    val TEXTURE_COLLECTIONS: Registry<TextureCollection> = Registry.create(TextureCollection::class.java, ResourceLocation("bubbleblaster", "texture_collections"))
}