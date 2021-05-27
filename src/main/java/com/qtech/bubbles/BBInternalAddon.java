package com.qtech.bubbles;

import com.qtech.bubbles.bubble.AbstractBubble;
import com.qtech.bubbles.common.GraphicsProcessor;
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
import com.qtech.bubbles.init.*;
import com.qtech.bubbles.registry.Registers;
import com.qtech.bubbles.screen.LoadScreen;
import com.qtech.bubbles.settings.GameSettings;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.util.Collection;

@Addon(addonId = "bubbleblaster")
public class BBInternalAddon extends QBubblesAddon {
    public static final String ADDON_ID = "bubbleblaster";

    public final LocalAddonEventBus<? extends QBubblesAddon> eventBus;

    public BBInternalAddon(Logger logger, String addonId, AddonObject<BBInternalAddon> addonObject) {
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
                    ResourceLocation resourceLocation = new ResourceLocation(bubble.getRegistryName().getNamespace(), bubble.getRegistryName().getPath() + "/" + i);
                    final int finalI = i;
                    textureCollection.set(resourceLocation, new ITexture() {
                        @Override
                        public void render(GraphicsProcessor gp) {
                            gp.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                            gp.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                            EnvironmentRenderer.drawBubble(gp, 0, 0, finalI, bubble.colors);

                            gp.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
                            gp.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
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
