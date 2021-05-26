package com.qtech.bubbles.common.cursor;

import com.qtech.bubbles.common.GraphicsProcessor;
import com.qtech.bubbles.common.renderer.CursorRenderer;

import java.awt.*;

public class BlankCursorRenderer extends CursorRenderer {
    public BlankCursorRenderer() {
        super("blank_cursor");
    }

    @Override
    public void draw(GraphicsProcessor g) {

    }
}
