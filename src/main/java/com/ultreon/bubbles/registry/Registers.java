package com.ultreon.bubbles.registry;

import com.google.common.annotations.Beta;
import com.ultreon.bubbles.bubble.AbstractBubble;
import com.ultreon.bubbles.common.gamestate.GameEvent;
import com.ultreon.bubbles.common.gametype.AbstractGameType;
import com.ultreon.bubbles.effect.StatusEffect;
import com.ultreon.bubbles.entity.ammo.AmmoType;
import com.ultreon.bubbles.entity.player.ability.AbilityType;
import com.ultreon.bubbles.entity.types.EntityType;
import com.ultreon.bubbles.init.*;
import com.ultreon.bubbles.item.ItemType;
import com.ultreon.hydro.common.ResourceEntry;
import com.ultreon.hydro.registry.DeferredRegister;
import com.ultreon.hydro.registry.Registry;
import com.ultreon.hydro.render.TextureCollection;
import com.ultreon.hydro.screen.gui.cursor.RegistrableCursor;

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
    public static final Registry<AbstractBubble> BUBBLES = Registry.create(AbstractBubble.class, new ResourceEntry("bubbleblaster", "bubbles"));

    /**
     * Ammo type register.
     * Ammo types are what it says ammo types, you can create different ammo types doing different things.
     *
     * @see AmmoTypes
     */
    public static final Registry<AmmoType> AMMO_TYPES = Registry.create(AmmoType.class, new ResourceEntry("bubbleblaster", "ammo_types"));

    /**
     * Entity register.
     * Entities are one of the core things in a game, they are used for creating the player, enemies or in this case also bubbles.
     *
     * @see Entities
     */
    public static final Registry<EntityType> ENTITIES = Registry.create(EntityType.class, new ResourceEntry("bubbleblaster", "entities"));

    /**
     * Effect register.
     * Effects are classes for doing things over time after activating, and can stop after some time.
     *
     * @see Effects
     */
    public static final Registry<StatusEffect> EFFECTS = Registry.create(StatusEffect.class, new ResourceEntry("bubbleblaster", "effects"));

    /**
     * Ability register.
     * Abilities are classes for doing things like teleportation.
     *
     * @see Abilities
     */
    public static final Registry<AbilityType> ABILITIES = Registry.create(AbilityType.class, new ResourceEntry("bubbleblaster", "abilities"));

    /**
     * Game state register.
     *
     * @see GameEvents
     */
    public static final Registry<GameEvent> GAME_EVENTS = Registry.create(GameEvent.class, new ResourceEntry("bubbleblaster", "game_states"));

    /**
     * Game type register.
     *
     * @see GameTypes
     */
    public static final Registry<AbstractGameType> GAME_TYPES = Registry.create(AbstractGameType.class, new ResourceEntry("bubbleblaster", "game_types"));

    /**
     * Cursor register.
     */
    public static final Registry<RegistrableCursor> CURSORS = Registry.create(RegistrableCursor.class, new ResourceEntry("bubbleblaster", "cursors"));

    /**
     * Items register, will be used in the future.
     */
    @Beta
    public static final Registry<ItemType> ITEMS = Registry.create(ItemType.class, new ResourceEntry("bubbleblaster", "items"));

    /**
     * Texture collection register.
     * Used for mapping and preloading textures.
     *
     * @see TextureCollections
     */
    public static final Registry<TextureCollection> TEXTURE_COLLECTIONS = Registry.create(TextureCollection.class, new ResourceEntry("bubbleblaster", "texture_collections"));
}
