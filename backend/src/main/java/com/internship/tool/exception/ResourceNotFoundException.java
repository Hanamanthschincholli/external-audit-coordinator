package com.internship.tool.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    // ✅ THIS FIXES YOUR ERROR
    public ResourceNotFoundException(String resourceName, String fieldName, Object value) {
        super(resourceName + " not found with " + fieldName + " : " + value);
    }
    
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
