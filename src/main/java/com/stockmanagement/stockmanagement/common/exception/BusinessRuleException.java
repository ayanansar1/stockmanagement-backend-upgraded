package com.stockmanagement.stockmanagement.common.exception;


public class BusinessRuleException
        extends RuntimeException {

    public BusinessRuleException(String message) {
        super(message);
    }
}