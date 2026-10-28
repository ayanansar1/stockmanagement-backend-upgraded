package com.stockmanagement.stockmanagement.common.exception;

public class ResourceNotFoundException extends RuntimeException {
    private final String code;

    public ResourceNotFoundException(String message) {
        this("RESOURCE_NOT_FOUND", message);
    }

    public ResourceNotFoundException(String code, String message) {
        super(message);
        this.code = code;
    }

    public String getCode() { return code; }
}



//public class ResourceNotFoundException
//        extends RuntimeException {
//
//    public ResourceNotFoundException(String message) {
//        super(message);
//    }
//}
