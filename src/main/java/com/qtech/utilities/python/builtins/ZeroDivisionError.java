package com.qtech.utilities.python.builtins;

public class ZeroDivisionError extends RuntimeException {
    public ZeroDivisionError() {
    }

    public ZeroDivisionError(String message) {
        super(message);
    }

    public ZeroDivisionError(String message, Throwable cause) {
        super(message, cause);
    }

    public ZeroDivisionError(Throwable cause) {
        super(cause);
    }

    public ZeroDivisionError(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
