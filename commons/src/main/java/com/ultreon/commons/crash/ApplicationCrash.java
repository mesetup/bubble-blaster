package com.ultreon.commons.crash;

import com.ultreon.commons.util.StringUtils;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public final class ApplicationCrash extends RuntimeException {
    private static final List<Runnable> crashHandlers = new ArrayList<>();
    private final CrashLog crashLog;

    ApplicationCrash(CrashLog crashLog) {
        this.crashLog = crashLog;
    }

    @Override
    public void printStackTrace() {
        printCrash();
        crash();
    }

    @Override
    public void printStackTrace(PrintWriter err) {
        printCrash();
        crash();
    }

    @Override
    public void printStackTrace(PrintStream err) {
        printCrash();
        crash();
    }

    public void printCrash() {
        String crashString = this.crashLog.toString();
        List<String> strings = StringUtils.splitIntoLines(crashString);
        for (String string : strings) {
            System.err.println(string);
        }
    }

    private void crash() {
        for (Runnable handler : crashHandlers) {
            handler.run();
        }
    }

    public static void onCrash(Runnable handler) {
        crashHandlers.add(handler);
    }

    public CrashLog getCrashLog() {
        return crashLog;
    }
}
