package com.qsoftware.bubbles;

import com.qsoftware.bubbles.common.addon.Addon;
import com.qsoftware.bubbles.common.addon.AddonObject;
import com.qsoftware.bubbles.common.addon.QBubblesAddon;
import com.qsoftware.bubbles.event.Bus;
import com.qsoftware.bubbles.event.bus.LocalAddonEventBus;
import com.qsoftware.bubbles.init.*;
import org.apache.logging.log4j.Logger;

@Addon(addonId = "qbubbles")
public class QInternalAddon extends QBubblesAddon {
    public static final String ADDON_ID = "qbubbles";

    public final LocalAddonEventBus<? extends QBubblesAddon> eventBus;

    public QInternalAddon(Logger logger, String addonId, AddonObject<QInternalAddon> addonObject) {
        super(logger, addonId, addonObject);

        eventBus = Bus.getLocalAddonEventBus(this);
//        Attributes.ATTRIBUTES.register(eventBus);
//        Modifiers.MODIFIERS.register(eventBus);
        BubbleInit.BUBBLES.register(eventBus);
        AmmoInit.AMMO_TYPES.register(eventBus);
        EntityInit.ENTITIES.register(eventBus);
        EffectInit.EFFECTS.register(eventBus);
        AbilityInit.ABILITY_TYPES.register(eventBus);
        ScreenInit.SCREENS.register(eventBus);
//        GameStateInit.STATES.register(eventBus);
        GameTypeInit.GAME_TYPES.register(eventBus);
//        SceneInit.SCENES.register(eventBus);
//        Locales.register(eventBus);
    }
}
