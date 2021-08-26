package com.qtech.bubbles;

import com.qtech.bubbles.bubble.AbstractBubble;
import com.qtech.bubbles.common.ResourceLocation;
import com.qtech.bubbles.common.addon.Addon;
import com.qtech.bubbles.common.addon.AddonObject;
import com.qtech.bubbles.common.addon.QBubblesAddon;
import com.qtech.bubbles.environment.EnvironmentRenderer;
import com.qtech.bubbles.event.Bus;
import com.qtech.bubbles.event.SubscribeEvent;
import com.qtech.bubbles.event.TextureRenderEvent;
import com.qtech.bubbles.event.bus.LocalAddonEventBus;
import com.qtech.bubbles.graphics.ITexture;
import com.qtech.bubbles.graphics.TextureCollection;
import com.qtech.bubbles.registry.Registers;
import com.qtech.bubbles.screen.LoadScreen;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.util.Collection;

@Addon(addonId = "qbubbles")
public class QInternalAddon extends QBubblesAddon {
    public static final String ADDON_ID = "qbubbles";

    public final LocalAddonEventBus<? extends QBubblesAddon> eventBus;

    public QInternalAddon(Logger logger, String addonId, AddonObject<QInternalAddon> addonObject) {
        super(logger, addonId, addonObject);

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

    @SubscribeEvent
    public void onTextureRender(TextureRenderEvent textureRenderEvent) {
        TextureCollection textureCollection = textureRenderEvent.getTextureCollection();
        if (textureCollection == TextureCollections.BUBBLE_TEXTURES.get()) {
            Collection<AbstractBubble> bubbles = Registers.BUBBLES.values();
            LoadScreen loadScreen = LoadScreen.get();

            if (loadScreen == null) {
                throw new IllegalStateException("Load scene is not available.");
            }
            for (AbstractBubble bubble : bubbles) {
                loadScreen.logInfo("Loading bubble textures: " + bubble.getRegistryName());
                for (int i = 0; i <= bubble.getMaxRadius(); i++) {
                    ResourceLocation resourceLocation = new ResourceLocation(bubble.getRegistryName().namespace(), bubble.getRegistryName().path() + "/" + i);
                    final int finalI = i;
                    textureCollection.set(resourceLocation, new ITexture() {
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
