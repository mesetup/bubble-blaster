package com.ultreon.hydro;

import com.ultreon.commons.crash.CrashLog;
import com.ultreon.commons.crash.ApplicationCrash;

@SuppressWarnings("ClassCanBeRecord")
class GameExceptionHandler implements Thread.UncaughtExceptionHandler {
    private final Game game;

    GameExceptionHandler(Game game) {
        this.game = game;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        if (throwable instanceof ApplicationCrash) {
            game.close();
            throwable.printStackTrace();
            return;
        }
        CrashLog crashLog = new CrashLog("Uncaught exception", throwable);
        crashLog.createCrash().printStackTrace();
        game.close();
    }
}
