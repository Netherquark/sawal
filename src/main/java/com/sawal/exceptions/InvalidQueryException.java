// InvalidQueryException.java
package com.sawal.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import java.util.Map;

/**
 * Thrown when a natural-language request is ill-formed, incomplete,
 * or cannot be safely translated into a SQL query.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidQueryException extends QueryException {
    private static final long serialVersionUID = 4L;
    private static final String CODE = "INVALID_QUERY";

    public InvalidQueryException(String message) {
        super(CODE, message, HttpStatus.BAD_REQUEST);
    }

    public InvalidQueryException(String message, Throwable cause) {
        super(CODE, message, cause, HttpStatus.BAD_REQUEST);
    }

    public InvalidQueryException(String message, Map<String, Object> details) {
        super(CODE, message, HttpStatus.BAD_REQUEST, details);
    }

    public InvalidQueryException(String message, Throwable cause, Map<String, Object> details) {
        super(CODE, message, cause, HttpStatus.BAD_REQUEST, details);
    }
}
