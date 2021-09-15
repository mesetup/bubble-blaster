package com.ultreon.commons.utilities.python.builtins;

public class PlatformError extends RuntimeException {
    public PlatformError() {
    }

    public PlatformError(String message) {
        super(message);
    }

    public PlatformError(String message, Throwable cause) {
        super(message, cause);
    }

    public PlatformError(Throwable cause) {
        super(cause);
    }

    public PlatformError(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
