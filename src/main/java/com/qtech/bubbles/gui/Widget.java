package com.qtech.bubbles.gui;

import com.qtech.bubbles.common.GraphicsProcessor;

import java.awt.*;

public abstract class Widget {

    public abstract void paint(GraphicsProcessor g);

    public abstract void destroy();
}
