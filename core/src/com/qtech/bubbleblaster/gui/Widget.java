package com.qtech.bubbleblaster.gui;

import com.qtech.bubbleblaster.common.GraphicsProcessor;

public abstract class Widget {

    public abstract void paint(GraphicsProcessor g);

    public abstract void destroy();
}
