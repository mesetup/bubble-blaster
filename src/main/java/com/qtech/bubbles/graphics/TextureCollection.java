package com.qtech.bubbles.graphics;

import com.qtech.bubbles.common.ResourceEntry;
import com.qtech.bubbles.registry.RegistryEntry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class TextureCollection extends RegistryEntry {
    private static final Logger LOGGER = LogManager.getLogger("QB:TextureCollection");
    private final HashMap<ResourceEntry, Image> textures = new HashMap<>();

    public TextureCollection() {

    }

    public void set(ResourceEntry location, ITexture texture) {
        if (textures.containsKey(location)) {
            LOGGER.warn("Texture override: " + location);
        }

        BufferedImage bufferedImage = new BufferedImage(texture.width(), texture.height(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = (Graphics2D) bufferedImage.getGraphics();
        texture.render(graphics);
        graphics.dispose();

        textures.put(location, bufferedImage);
    }

    public Image get(ResourceEntry location) {
        return textures.get(location);
    }
}
