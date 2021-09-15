package com.ultreon.preloader.bubbleblaster;

import com.ultreon.preloader.IGameLoader;

@SuppressWarnings("unused")
public class QBubblesLoader implements IGameLoader {
    @Override
    public String getLoadingTarget() {
        return "com.ultreon.bubbles.Main";
    }

}
