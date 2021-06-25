package com.qtech.bubbles.util.python.builtins;

public class ValueError extends Throwable {
    public ValueError() {
    }

    public ValueError(String message) {
        super(message);
    }

    public ValueError(String message, Throwable cause) {
        super(message, cause);
    }

    public ValueError(Throwable cause) {
        super(cause);
    }

    public ValueError(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
