package com.shellApp.shellapp.exception;

public class ShellExceptionHandler extends RuntimeException {
    public ShellExceptionHandler(String message) {
        super(message);
    }

    public ShellExceptionHandler(String message, Throwable cause) {
        super(message, cause);
    }
}