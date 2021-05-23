package com.qtech.bubbles;

import com.qtech.bubbles.common.crash.CrashReport;
import com.qtech.bubbles.common.crash.ReportedException;

public class QBubblesExceptionHandler implements Thread.UncaughtExceptionHandler {
    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        if (throwable instanceof ReportedException) {
            QBubbles.getInstance().close();
            throwable.printStackTrace();
            return;
        }
        CrashReport crashReport = new CrashReport("Uncaught exception", throwable);
        crashReport.getReportedException().printStackTrace();
        QBubbles.getInstance().close();
    }
}
