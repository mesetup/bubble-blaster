package com.qtech.bubbles.registry;

import com.google.common.annotations.Beta;
import com.qtech.bubbles.bubble.AbstractBubble;
import com.qtech.bubbles.common.ResourceLocation;
import com.qtech.bubbles.common.ability.AbilityType;
import com.qtech.bubbles.common.ammo.AmmoType;
import com.qtech.bubbles.common.cursor.RegistrableCursor;
import com.qtech.bubbles.common.effect.Effect;
import com.qtech.bubbles.common.gamestate.GameEvent;
import com.qtech.bubbles.common.gametype.AbstractGameType;
import com.qtech.bubbles.common.screen.ScreenType;
import com.qtech.bubbles.entity.types.EntityType;
import com.qtech.bubbles.graphics.TextureCollection;
import com.qtech.bubbles.init.*;
import com.qtech.bubbles.item.ItemType;

/**
 * Registers, use this class to create deferred registers for specific things in the game.
 *
 * @see DeferredRegister
 */
@SuppressWarnings({"rawtypes", "deprecation"})
public class Registers {
    /**
     * Bubble register.
     * Bubbles are the core mechanic of the game, without the bubbles the game shouldn't exist.
     *
     * @see Bubbles
     */
    public static final Registry<AbstractBubble> BUBBLES = Registry.create(AbstractBubble.class, new ResourceLocation("bubbleblaster", "bubbles"));

    /**
     * Ammo type register.
     * Ammo types are what it says ammo types, you can create different ammo types doing different things.
     *
     * @see AmmoTypes
     */
    public static final Registry<AmmoType> AMMO_TYPES = Registry.create(AmmoType.class, new ResourceLocation("bubbleblaster", "ammo_types"));

    /**
     * Entity register.
     * Entities are one of the core things in a game, they are used for creating the player, enemies or in this case also bubbles.
     *
     * @see Entities
     */
    public static final Registry<EntityType> ENTITIES = Registry.create(EntityType.class, new ResourceLocation("bubbleblaster", "entities"));

    /**
     * Effect register.
     * Effects are classes for doing things over time after activating, and can stop after some time.
     *
     * @see Effects
     */
    public static final Registry<Effect> EFFECTS = Registry.create(Effect.class, new ResourceLocation("bubbleblaster", "effects"));

    /**
     * Ability register.
     * Abilities are classes for doing things like teleportation.
     *
     * @see Abilities
     */
    public static final Registry<AbilityType> ABILITIES = Registry.create(AbilityType.class, new ResourceLocation("bubbleblaster", "abilities"));

    /**
     * Screen register, will be unregistrable in the future.
     *
     * @see Screens
     */
    @Deprecated
    public static final Registry<ScreenType> SCREENS = Registry.create(ScreenType.class, new ResourceLocation("bubbleblaster", "screens"));

    /**
     * Game state register.
     *
     * @see GameEvents
     */
    public static final Registry<GameEvent> GAME_EVENTS = Registry.create(GameEvent.class, new ResourceLocation("bubbleblaster", "game_states"));

    /**
     * Game type register.
     *
     * @see GameTypes
     */
    public static final Registry<AbstractGameType> GAME_TYPES = Registry.create(AbstractGameType.class, new ResourceLocation("bubbleblaster", "game_types"));

    /**
     * Cursor register.
     */
    public static final Registry<RegistrableCursor> CURSORS = Registry.create(RegistrableCursor.class, new ResourceLocation("bubbleblaster", "cursors"));

    /**
     * Items register, will be used in the future.
     */
    @Beta
    public static final Registry<ItemType> ITEMS = Registry.create(ItemType.class, new ResourceLocation("bubbleblaster", "items"));

    /**
     * Texture collection register.
     * Used for mapping and preloading textures.
     *
     * @see TextureCollections
     */
    public static final Registry<TextureCollection> TEXTURE_COLLECTIONS = Registry.create(TextureCollection.class, new ResourceLocation("bubbleblaster", "texture_collections"));
}
