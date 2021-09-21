package com.ultreon.bubbles;

import com.ultreon.bubbles.bubble.AbstractBubble;
import com.ultreon.bubbles.common.mod.Mod;
import com.ultreon.bubbles.common.mod.ModInstance;
import com.ultreon.bubbles.common.mod.ModObject;
import com.ultreon.bubbles.environment.EnvironmentRenderer;
import com.ultreon.bubbles.event.bus.EventManagers;
import com.ultreon.bubbles.event.bus.ModEvents;
import com.ultreon.bubbles.init.*;
import com.ultreon.bubbles.registry.Registers;
import com.ultreon.bubbles.screen.LoadScreen;
import com.ultreon.hydro.common.ResourceEntry;
import com.ultreon.hydro.event.CollectTexturesEvent;
import com.ultreon.hydro.event.SubscribeEvent;
import com.ultreon.hydro.graphics.ITexture;
import com.ultreon.hydro.render.Renderer;
import com.ultreon.hydro.render.TextureCollection;
import org.apache.logging.log4j.Logger;

import java.util.Collection;

@Mod(modId = "bubbleblaster")
public class InternalMod extends ModInstance {
    public static final String MOD_ID = "bubbleblaster";

    public final ModEvents<? extends ModInstance> eventBus;

    public InternalMod(Logger logger, String modId, ModObject<InternalMod> modObject) {
        super(logger, modId, modObject);

        eventBus = EventManagers.getModEvents(this);
        EventManagers.getGameEvents().subscribe(this);
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
                        public void render(Renderer gg) {
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
