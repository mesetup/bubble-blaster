package com.ultreon.hydro.screen.gui.cursor;

import com.ultreon.hydro.common.Drawable;
import com.ultreon.hydro.common.ResourceEntry;
import com.ultreon.hydro.registry.Registry;
import com.ultreon.hydro.render.Renderer;

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

        Renderer gg2 = new Renderer(cursorImg2.createGraphics());
        draw(gg2);

        // Create a new blank cursor.
        Cursor cursor = Toolkit.getDefaultToolkit().createCustomCursor(
                cursorImg2, new Point(11, 11), name);

        Registry.getRegistry(RegistrableCursor.class).register(ResourceEntry.fromString("qbubbles:" + name), new RegistrableCursor(cursor));

        return cursor;
    }

    @Override
    public abstract void draw(Renderer g);

    public String getName() {
        return name;
    }
}
