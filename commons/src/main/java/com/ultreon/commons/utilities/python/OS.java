package com.ultreon.commons.utilities.python;

import com.ultreon.commons.utilities.system.User;
import lombok.experimental.UtilityClass;

import java.io.IOException;

@UtilityClass
@SuppressWarnings("UnusedReturnValue")
public final class OS {
    public static String getSep() {
        return System.getProperty("file.separator");
    }

    public static String getLogin() {
        return System.getProperty("user.name");
    }

    public static User getUser() {
        return new User();
    }

    public static boolean kill(long pid) {
        ProcessHandle process = ProcessHandle.of(pid).orElseThrow();
        return process.destroy();
    }

    public static long getPID() {
        ProcessHandle process = ProcessHandle.current();
        return process.pid();
    }

    public static int system(String cmd) {
        Process process;
        try {
            process = Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {
            e.printStackTrace();
            return 1;
        }

        int exitCode;
        try {
            exitCode = process.waitFor();
        } catch (InterruptedException ignored) {
            exitCode = -1;
        }

        return exitCode;
    }

    public static long getTID() {
        return Thread.currentThread().getId();
    }

    public static long getTID(Thread thread) {
        return thread.getId();
    }

    public static boolean killThread() {
        return killThread(Thread.currentThread());
    }

    public static boolean killThread(long tid) {
        for (Thread t : Thread.getAllStackTraces().keySet()) {
            if (t.getId() == tid) {
                t.interrupt();
                return t.isInterrupted();
            }
        }
        return false;
    }

    public static boolean killThread(Thread thread) {
        thread.interrupt();
        return thread.isInterrupted();
    }
}
