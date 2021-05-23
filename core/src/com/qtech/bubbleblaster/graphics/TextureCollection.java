package com.qtech.bubbleblaster.graphics;

import com.qtech.bubbleblaster.common.RegistryEntry;
import com.qtech.bubbleblaster.common.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;

public class TextureCollection extends RegistryEntry {
    private static final Logger LOGGER = LogManager.getLogger("QB:TextureCollection");
    private final HashMap<ResourceLocation, Image> textures = new HashMap<>();

    public TextureCollection() {

    }

    public void set(ResourceLocation location, ITexture texture) {
        if (textures.containsKey(location)) {
            LOGGER.warn("Texture override: " + location);
        }

        BufferedImage bufferedImage = new BufferedImage(texture.width(), texture.height(), BufferedImage.TYPE_INT_ARGB);
        GraphicsProcessor graphics = (GraphicsProcessor) bufferedImage.getGraphics();
        texture.render(graphics);
        graphics.dispose();

        textures.put(location, bufferedImage);
    }

    public Image get(ResourceLocation location) {
        return textures.get(location);
    }
}
