package com.qtech.bubbleblaster;

import com.qtech.bubbleblaster.bubble.AbstractBubble;
import com.qtech.bubbleblaster.common.ResourceLocation;
import com.qtech.bubbleblaster.common.addon.Addon;
import com.qtech.bubbleblaster.common.addon.AddonObject;
import com.qtech.bubbleblaster.common.addon.QBubblesAddon;
import com.qtech.bubbleblaster.environment.EnvironmentRenderer;
import com.qtech.bubbleblaster.event.Bus;
import com.qtech.bubbleblaster.event.SubscribeEvent;
import com.qtech.bubbleblaster.event.TextureRenderEvent;
import com.qtech.bubbleblaster.event.bus.LocalAddonEventBus;
import com.qtech.bubbleblaster.graphics.ITexture;
import com.qtech.bubbleblaster.graphics.TextureCollection;
import com.qtech.bubbleblaster.init.*;
import com.qtech.bubbleblaster.registry.Registers;
import com.qtech.bubbleblaster.screen.LoadScreen;
import org.apache.logging.log4j.Logger;

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
                    ResourceLocation resourceLocation = new ResourceLocation(bubble.getRegistryName().getNamespace(), bubble.getRegistryName().getPath() + "/" + i);
                    final int finalI = i;
                    textureCollection.set(resourceLocation, new ITexture() {
                        @Override
                        public void render(GraphicsProcessor gg) {
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
