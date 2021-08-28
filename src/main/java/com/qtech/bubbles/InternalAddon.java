package com.qtech.bubbles;

import com.qtech.bubbles.bubble.AbstractBubble;
import com.qtech.bubbles.common.ResourceEntry;
import com.qtech.bubbles.common.mod.Mod;
import com.qtech.bubbles.common.mod.ModInstance;
import com.qtech.bubbles.common.mod.ModObject;
import com.qtech.bubbles.environment.EnvironmentRenderer;
import com.qtech.bubbles.event.CollectTexturesEvent;
import com.qtech.bubbles.event._common.SubscribeEvent;
import com.qtech.bubbles.event.bus.Bus;
import com.qtech.bubbles.event.bus.LocalAddonEventBus;
import com.qtech.bubbles.graphics.ITexture;
import com.qtech.bubbles.graphics.TextureCollection;
import com.qtech.bubbles.init.*;
import com.qtech.bubbles.registry.Registers;
import com.qtech.bubbles.screen.LoadScreen;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.util.Collection;

@Mod(addonId = "qbubbles")
public class InternalAddon extends ModInstance {
    public static final String ADDON_ID = "qbubbles";

    public final LocalAddonEventBus<? extends ModInstance> eventBus;

    public InternalAddon(Logger logger, String addonId, ModObject<InternalAddon> modObject) {
        super(logger, addonId, modObject);

        eventBus = Bus.getLocalAddonEventBus(this);
        Bus.getQBubblesEventBus().register(this);
        Bubbles.BUBBLES.register(eventBus);
        AmmoTypes.AMMO_TYPES.register(eventBus);
        Entities.ENTITIES.register(eventBus);
        Effects.EFFECTS.register(eventBus);
        Abilities.ABILITY_TYPES.register(eventBus);
        GameEvents.GAME_EVENTS.register(eventBus);
        GameTypes.GAME_TYPES.register(eventBus);
        TextureCollections.register(eventBus);
    }

    /**
     * Event handler for collecting textures.
     *
     * @param event the event we are subscribed to.
     */
    @SubscribeEvent
    public void onCollectTextures(CollectTexturesEvent event) {
        TextureCollection textureCollection = event.getTextureCollection();
        if (textureCollection == TextureCollections.BUBBLE_TEXTURES.get()) {
            Collection<AbstractBubble> bubbles = Registers.BUBBLES.values();
            LoadScreen loadScreen = LoadScreen.get();

            if (loadScreen == null) {
                throw new IllegalStateException("Load scene is not available.");
            }
            for (AbstractBubble bubble : bubbles) {
                loadScreen.logInfo("Loading bubble textures: " + bubble.getRegistryName());
                for (int i = 0; i <= bubble.getMaxRadius(); i++) {
                    ResourceEntry resourceEntry = new ResourceEntry(bubble.getRegistryName().namespace(), bubble.getRegistryName().path() + "/" + i);
                    final int finalI = i;
                    textureCollection.set(resourceEntry, new ITexture() {
                        @Override
                        public void render(Graphics2D gg) {
                            EnvironmentRenderer.drawBubble(gg, 0, 0, finalI, bubble.colors);
                        }

                        @Override
                        public int width() {
                            return finalI + bubble.getColors().length * 2;
                        }

                        @Override
                        public int height() {
                            return finalI + bubble.getColors().length * 2;
                        }
                    });
                }
            }
        }
    }
}
