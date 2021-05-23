package com.qtech.bubbleblaster;

import com.qtech.bubbleblaster.common.crash.CrashReport;
import com.qtech.bubbleblaster.common.crash.ReportedException;

public class QBubblesExceptionHandler implements Thread.UncaughtExceptionHandler {
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
