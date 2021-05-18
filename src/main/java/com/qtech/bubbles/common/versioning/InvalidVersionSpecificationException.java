package com.qtech.bubbles.common.versioning;

public class InvalidVersionSpecificationException extends Exception {
    public InvalidVersionSpecificationException() {
    }

    public InvalidVersionSpecificationException(String message) {
        super(message);
    }

    public InvalidVersionSpecificationException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidVersionSpecificationException(Throwable cause) {
        super(cause);
    }

    public InvalidVersionSpecificationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
