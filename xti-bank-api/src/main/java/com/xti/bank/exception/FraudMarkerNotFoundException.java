package com.xti.bank.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown when a requested fraud marker was not found in the BCB DICT API.
 * Typically results in HTTP 404 Not Found.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class FraudMarkerNotFoundException extends RuntimeException {

    public FraudMarkerNotFoundException(String message) {
        super(message);
    }

    public FraudMarkerNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
