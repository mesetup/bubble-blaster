package com.ultreon.commons.utilities.python.builtins;

public class OSError extends RuntimeException {
    public OSError() {
    }

    public OSError(String message) {
        super(message);
    }

    public OSError(String message, Throwable cause) {
        super(message, cause);
    }

    public OSError(Throwable cause) {
        super(cause);
    }

    public OSError(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
