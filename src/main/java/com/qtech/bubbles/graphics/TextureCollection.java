package com.qtech.bubbles.graphics;

import com.qtech.bubbles.common.GraphicsProcessor;
import com.qtech.bubbles.common.RegistryEntry;
import com.qtech.bubbles.common.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class TextureCollection extends RegistryEntry {
    private static final Logger LOGGER = LogManager.getLogger("BB:TextureCollection");
    private final HashMap<ResourceLocation, Image> textures = new HashMap<>();

    public TextureCollection() {

    }

    public void set(ResourceLocation location, ITexture texture) {
        if (textures.containsKey(location)) {
            LOGGER.warn("Texture override: " + location);
        }

        BufferedImage bufferedImage = new BufferedImage(texture.width(), texture.height(), BufferedImage.TYPE_INT_ARGB);
        GraphicsProcessor graphics = new GraphicsProcessor(bufferedImage.getGraphics());
        texture.render(graphics);
        graphics.dispose();

        textures.put(location, bufferedImage);
    }

    public Image get(ResourceLocation location) {
        return textures.get(location);
    }
}
