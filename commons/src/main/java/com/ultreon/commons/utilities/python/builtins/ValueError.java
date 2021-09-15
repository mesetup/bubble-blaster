package com.ultreon.commons.utilities.python.builtins;

public class ValueError extends RuntimeException {
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
