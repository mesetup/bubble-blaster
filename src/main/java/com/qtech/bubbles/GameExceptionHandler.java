package com.qtech.bubbles;

import com.qtech.bubbles.common.crash.CrashReport;
import com.qtech.bubbles.common.crash.ReportedException;

public class GameExceptionHandler implements Thread.UncaughtExceptionHandler {
    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        if (throwable instanceof ReportedException) {
            BubbleBlaster.getInstance().close();
            throwable.printStackTrace();
            return;
        }
        CrashReport crashReport = new CrashReport("Uncaught exception", throwable);
        crashReport.getReportedException().printStackTrace();
        BubbleBlaster.getInstance().close();
    }
}
