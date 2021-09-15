package com.ultreon.hydro.render;

import com.ultreon.hydro.common.RegistryEntry;
import com.ultreon.hydro.common.ResourceEntry;
import com.ultreon.hydro.graphics.ITexture;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class TextureCollection extends RegistryEntry {
    private static final Logger LOGGER = LogManager.getLogger("Texture-Collection");
    private final HashMap<ResourceEntry, Image> textures = new HashMap<>();

    public TextureCollection() {

    }

    public void set(ResourceEntry location, ITexture texture) {
        if (textures.containsKey(location)) {
            LOGGER.warn("Texture override: " + location);
        }

        BufferedImage bufferedImage = new BufferedImage(texture.width(), texture.height(), BufferedImage.TYPE_INT_ARGB);
        Renderer graphics = new Renderer(bufferedImage.getGraphics());
        graphics.hint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        texture.render(graphics);
        graphics.dispose();

        textures.put(location, bufferedImage);
    }

    public Image get(ResourceEntry location) {
        return textures.get(location);
    }

    public String toString() {
        return "TextureCollection[" + textures.size() + " textures]";
    }
}
