package com.ultreon.commons.crash;

import com.ultreon.commons.util.StringUtils;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public final class GameCrash extends RuntimeException {
    private static final List<Runnable> crashHandlers = new ArrayList<>();
    private final CrashReport crashReport;

    GameCrash(CrashReport crashReport) {
        this.crashReport = crashReport;
    }

    @Override
    public void printStackTrace() {
        String crashString = this.crashReport.toString();
        List<String> strings = StringUtils.splitIntoLines(crashString);
        for (String string : strings) {
            System.err.println(string);
        }

        crash();
    }

    @Override
    public void printStackTrace(PrintWriter err) {
        String crashString = this.crashReport.toString();
        List<String> strings = StringUtils.splitIntoLines(crashString);
        for (String string : strings) {
            err.println(string);
        }

        crash();
    }

    @Override
    public void printStackTrace(PrintStream err) {
        String crashString = this.crashReport.toString();
        List<String> strings = StringUtils.splitIntoLines(crashString);
        for (String string : strings) {
            err.println(string);
        }

        crash();
    }

    private void crash() {
        for (Runnable handler : crashHandlers) {
            handler.run();
        }
    }

    public static void onCrash(Runnable handler) {
        crashHandlers.add(handler);
    }

    public CrashReport getCrashReport() {
        return crashReport;
    }
}
