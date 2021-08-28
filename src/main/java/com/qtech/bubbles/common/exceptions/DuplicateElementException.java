package com.qtech.bubbles.common.exceptions;

public class DuplicateElementException extends RuntimeException {
    public DuplicateElementException() {
        super();
    }

    public DuplicateElementException(String message) {
        super(message);
    }

    public DuplicateElementException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateElementException(Throwable cause) {
        super(cause);
    }
}
