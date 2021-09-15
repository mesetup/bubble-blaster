package com.ultreon.hydro.gui;

public abstract class AbstractButton extends Widget {
    public abstract Runnable getCommand();

    public abstract void setCommand(Runnable runnable);
}
