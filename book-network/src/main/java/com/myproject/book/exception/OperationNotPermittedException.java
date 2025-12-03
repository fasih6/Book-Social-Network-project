package com.myproject.book.exception;

public class OperationNotPermittedException extends RuntimeException {
    public OperationNotPermittedException(String msg) {
        super(msg);
    }// now you need to handle this exception in GlobalExceptionHandler
}
