package com.qtech.bubbles.core.exceptions;

public class ClassExistsError extends Exception {
    public ClassExistsError(String errorMessage) {
        super(errorMessage);
    }
}