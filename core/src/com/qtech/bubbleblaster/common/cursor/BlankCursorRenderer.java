package com.qtech.bubbleblaster.common.cursor;

import com.qtech.bubbleblaster.common.renderer.CursorRenderer;


public class BlankCursorRenderer extends CursorRenderer {
    public BlankCursorRenderer() {
        super("blank_cursor");
    }

    @Override
    public void draw(Graphics g) {

    }
}
