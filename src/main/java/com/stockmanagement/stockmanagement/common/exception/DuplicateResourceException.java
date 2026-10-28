package com.stockmanagement.stockmanagement.common.exception;


public class DuplicateResourceException
        extends RuntimeException {

    public DuplicateResourceException(String message) {
        super(message);
    }
}