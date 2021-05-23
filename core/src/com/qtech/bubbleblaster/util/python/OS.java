package com.qtech.bubbleblaster.util.python;

import com.qtech.bubbleblaster.util.system.User;

public interface OS {
    static String getSep() {
        return System.getProperty("file.separator");
    }

    static String getLogin() {
        return System.getProperty("user.name");
    }

    static User getUser() {
        return new User();
    }

    static boolean kill(long pid) {
        ProcessHandle process = ProcessHandle.of(pid).orElseThrow(() -> new RuntimeException("Process not found!"));
        return process.destroy();
    }

    static long getPID() {
        ProcessHandle process = ProcessHandle.current();
        return process.pid();
    }

    static long getTID() {
        return Thread.currentThread().getId();
    }

    static long getTID(Thread thread) {
        return thread.getId();
    }

    static boolean killThread() {
        return killThread(Thread.currentThread());
    }

    static boolean killThread(long tid) {
        for (Thread t : Thread.getAllStackTraces().keySet()) {
            if (t.getId() == tid) {
                t.interrupt();
                return t.isInterrupted();
            }
        }
        return false;
    }

    static boolean killThread(Thread thread) {
        thread.interrupt();
        return thread.isInterrupted();
    }
}
