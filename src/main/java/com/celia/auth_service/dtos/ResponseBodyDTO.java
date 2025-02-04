package com.celia.auth_service.dtos;

public record ResponseBodyDTO<T>(
        String message,    // Message describing the result
        T data,            // Data payload
        int status,        // HTTP status code
        Object metadata    // Additional metadata
) {
}