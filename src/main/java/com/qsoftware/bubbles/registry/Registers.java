package com.qsoftware.bubbles.registry;

import com.qsoftware.bubbles.bubble.AbstractBubble;
import com.qsoftware.bubbles.common.ResourceLocation;
import com.qsoftware.bubbles.common.ability.AbilityType;
import com.qsoftware.bubbles.common.ammo.AmmoType;
import com.qsoftware.bubbles.common.cursor.RegistrableCursor;
import com.qsoftware.bubbles.common.effect.Effect;
import com.qsoftware.bubbles.common.gamestate.GameEvent;
import com.qsoftware.bubbles.common.gametype.AbstractGameType;
import com.qsoftware.bubbles.common.screen.ScreenType;
import com.qsoftware.bubbles.entity.types.EntityType;
import com.qsoftware.bubbles.item.ItemType;

@SuppressWarnings({"rawtypes", "deprecation"})
public class Registers {
    //    public static final Registry<Attribute> ATTRIBUTES = Registry.createRegistry(Attribute.class);
//    public static final Registry<Modifier> MODIFIERS = Registry.createRegistry(Modifier.class);
    public static final Registry<AbstractBubble> BUBBLES = Registry.create(AbstractBubble.class, new ResourceLocation("qbubbles", "bubbles"));
    public static final Registry<AmmoType> AMMO_TYPES = Registry.create(AmmoType.class, new ResourceLocation("qbubbles", "ammo_types"));
    public static final Registry<EntityType> ENTITIES = Registry.create(EntityType.class, new ResourceLocation("qbubbles", "entities"));
    public static final Registry<Effect> EFFECTS = Registry.create(Effect.class, new ResourceLocation("qbubbles", "effects"));
    public static final Registry<AbilityType> ABILITIES = Registry.create(AbilityType.class, new ResourceLocation("qbubbles", "abilities"));
    public static final Registry<ScreenType> SCREENS = Registry.create(ScreenType.class, new ResourceLocation("qbubbles", "screens"));
    public static final Registry<GameEvent> STATES = Registry.create(GameEvent.class, new ResourceLocation("qbubbles", "game_states"));
    public static final Registry<AbstractGameType> GAME_TYPES = Registry.create(AbstractGameType.class, new ResourceLocation("qbubbles", "game_types"));
    //    public static final Registry<Scene> SCENES = Registry.createRegistry(Scene.class, new ResourceLocation("qbubbles", "scenes"));
//    public static final Registry<Locale> LOCALES = Registry.createRegistry(Locale.class);
    public static final Registry<RegistrableCursor> CURSORS = Registry.create(RegistrableCursor.class, new ResourceLocation("qbubbles", "cursors"));
    public static final Registry<ItemType> ITEMS = Registry.create(ItemType.class, new ResourceLocation("qbubbles", "items"));
}
