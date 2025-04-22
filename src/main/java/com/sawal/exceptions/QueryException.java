// QueryException.java
package com.sawal.exceptions;

import org.springframework.http.HttpStatus;
import java.time.Instant;
import java.util.Collections;
import java.util.Map;

/**
 * Base exception for all application-specific errors, carrying an error code,
 * HTTP status, timestamp, optional details, and cause.
 */
public abstract class QueryException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private final String errorCode;
    private final HttpStatus httpStatus;
    private final Instant timestamp;
    private final Map<String, Object> details;

    /**
     * Basic constructor without details or cause.
     */
    protected QueryException(String errorCode, String message, HttpStatus httpStatus) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.timestamp = Instant.now();
        this.details = Collections.emptyMap();
    }

    /**
     * Constructor with a nested cause but no additional details.
     */
    protected QueryException(String errorCode, String message, Throwable cause, HttpStatus httpStatus) {
        super(message, cause);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.timestamp = Instant.now();
        this.details = Collections.emptyMap();
    }

    /**
     * Constructor with additional metadata details but no cause.
     */
    protected QueryException(String errorCode, String message, HttpStatus httpStatus, Map<String, Object> details) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.timestamp = Instant.now();
        this.details = details != null ? Collections.unmodifiableMap(details) : Collections.emptyMap();
    }

    /**
     * Full constructor with message, cause, and metadata details.
     */
    protected QueryException(String errorCode, String message, Throwable cause, HttpStatus httpStatus, Map<String, Object> details) {
        super(message, cause);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.timestamp = Instant.now();
        this.details = details != null ? Collections.unmodifiableMap(details) : Collections.emptyMap();
    }

    public String getErrorCode() {
        return errorCode;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public Map<String, Object> getDetails() {
        return details;
    }

    @Override
    public String toString() {
        return String.format("%s{code='%s', status=%s, timestamp=%s, message='%s', details=%s}",
            getClass().getSimpleName(), errorCode, httpStatus, timestamp, getMessage(), details);
    }
}
