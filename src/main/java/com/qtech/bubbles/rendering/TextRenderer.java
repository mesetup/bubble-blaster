package com.qtech.bubbles.rendering;

import com.qtech.bubbles.BubbleBlaster;

import java.awt.*;
import java.awt.font.TextAttribute;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public abstract class TextRenderer {
    private final Graphics2D gfx;
    private Font font;

    public TextRenderer() {
        AtomicReference<Graphics2D> ref = new AtomicReference<>();
        this.createRenderer(ref);
        this.gfx = ref.get();
    }

    public void font(Font font, int size) {
        String fontName = font.getFontName();
        Map<TextAttribute, ?> attributes = font.getAttributes();
        int style = font.getStyle();

        HashMap<TextAttribute, Object> textAttributeHashMap = new HashMap<>(font.getAttributes());
        textAttributeHashMap.put(TextAttribute.SIZE, size * BubbleBlaster.getInstance().getRenderSettings().getScale());

        final Font finalFont = new Font(textAttributeHashMap);
        this.font = finalFont;
    }

    public void text(String text, int x, int y) {
        gfx.setFont(font);
        gfx.drawString(text, x, y);
    }

    protected abstract void createRenderer(AtomicReference<Graphics2D> reference);
}
