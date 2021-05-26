package com.qtech.bubbles.common.renderer;

import com.qtech.bubbles.common.GraphicsProcessor;
import com.qtech.bubbles.common.ResourceLocation;
import com.qtech.bubbles.common.cursor.RegistrableCursor;
import com.qtech.bubbles.common.interfaces.Drawable;
import com.qtech.bubbles.registry.Registry;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class CursorRenderer implements Drawable {
    private final String name;

    public CursorRenderer(String name) {
        this.name = name;
    }

    public final Cursor create() {

        // Transparent 16 x 16 pixel cursor image.
        BufferedImage cursorImg2 = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);

        GraphicsProcessor gg2 = new GraphicsProcessor(cursorImg2.createGraphics());
        draw(gg2);

        // Create a new blank cursor.
        Cursor cursor = Toolkit.getDefaultToolkit().createCustomCursor(
                cursorImg2, new Point(11, 11), name);

        Registry.getRegistry(RegistrableCursor.class).register(ResourceLocation.fromString("bubbleblaster:" + name), new RegistrableCursor(cursor));

        return cursor;
    }

    @Override
    public abstract void draw(GraphicsProcessor g);

    public String getName() {
        return name;
    }
}
