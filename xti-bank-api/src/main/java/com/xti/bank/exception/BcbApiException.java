package com.xti.bank.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * General exception for errors returned by the BCB DICT API.
 * Covers client errors (4xx), server errors (5xx), authentication issues, rate limits, etc.
 */
@ResponseStatus(HttpStatus.BAD_GATEWAY)  // 502 - common for upstream service errors
@Getter
public class BcbApiException extends RuntimeException {

    private final HttpStatusCode httpStatusCode;
    private final String bcbResponseBody;

    public BcbApiException(String message) {
        super(message);
        this.httpStatusCode = null;
        this.bcbResponseBody = null;
    }

    public BcbApiException(String message, HttpStatusCode httpStatusCode) {
        super(message);
        this.httpStatusCode = httpStatusCode;
        this.bcbResponseBody = null;
    }

    public BcbApiException(String message, HttpStatusCode httpStatusCode, String bcbResponseBody) {
        super(message);
        this.httpStatusCode = httpStatusCode;
        this.bcbResponseBody = bcbResponseBody;
    }

    public BcbApiException(String message, Throwable cause) {
        super(message, cause);
        this.httpStatusCode = null;
        this.bcbResponseBody = null;
    }

    @Override
    public String getMessage() {
        String base = super.getMessage();
        if (httpStatusCode != null) {
            return base + " (BCB Status: " + httpStatusCode.value() + ")";
        }
        return base;
    }
}
