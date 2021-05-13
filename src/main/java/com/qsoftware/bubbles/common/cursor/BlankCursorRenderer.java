package com.qsoftware.bubbles.common.cursor;

import com.qsoftware.bubbles.common.renderer.CursorRenderer;

import java.awt.*;

public class BlankCursorRenderer extends CursorRenderer {
    public BlankCursorRenderer() {
        super("blank_cursor");
    }

    @Override
    public void draw(Graphics g) {

    }
}
