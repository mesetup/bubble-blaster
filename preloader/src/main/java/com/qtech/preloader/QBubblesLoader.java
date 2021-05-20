package com.qtech.preloader;

@SuppressWarnings("unused")
public class QBubblesLoader implements IGameLoader {
    @Override
    public String getLoadingTarget() {
        return "com.qtech.bubbleblaster.desktop.DesktopLauncher";
    }

}
