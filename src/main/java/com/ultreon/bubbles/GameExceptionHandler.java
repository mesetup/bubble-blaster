package com.ultreon.bubbles;

import com.ultreon.commons.crash.CrashReport;
import com.ultreon.commons.crash.GameCrash;

public class GameExceptionHandler implements Thread.UncaughtExceptionHandler {
    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        if (throwable instanceof GameCrash) {
            BubbleBlaster.getInstance().close();
            throwable.printStackTrace();
            return;
        }
        CrashReport crashReport = new CrashReport("Uncaught exception", throwable);
        crashReport.getReportedException().printStackTrace();
        BubbleBlaster.getInstance().close();
    }
}
