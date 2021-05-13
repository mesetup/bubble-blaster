package com.qsoftware.bubbles.common.crash;

import com.qsoftware.bubbles.QBubbles;
import com.qsoftware.bubbles.core.utils.categories.StringUtils;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.List;

public final class ReportedException extends RuntimeException {
    private final CrashReport crashReport;

    ReportedException(CrashReport crashReport) {
        this.crashReport = crashReport;
    }

    @Override
    public void printStackTrace() {
        String crashString = this.crashReport.toString();
        List<String> strings = StringUtils.splitIntoLines(crashString);
        for (String string : strings) {
            System.err.println(string);
        }
        QBubbles.getInstance().shutdown();
    }

    @Override
    public void printStackTrace(PrintWriter err) {
        String crashString = this.crashReport.toString();
        List<String> strings = StringUtils.splitIntoLines(crashString);
        for (String string : strings) {
            err.println(string);
        }
        QBubbles.getInstance().shutdown();
    }

    @Override
    public void printStackTrace(PrintStream err) {
        String crashString = this.crashReport.toString();
        List<String> strings = StringUtils.splitIntoLines(crashString);
        for (String string : strings) {
            err.println(string);
        }
        QBubbles.getInstance().shutdown();
    }

    public CrashReport getCrashReport() {
        return crashReport;
    }
}
