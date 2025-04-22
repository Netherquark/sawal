// APIException.java
package com.sawal.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import java.util.Map;

/**
 * Thrown for failures in external API communication or response parsing.
 */
@ResponseStatus(HttpStatus.BAD_GATEWAY)
public class APIException extends QueryException {
    private static final long serialVersionUID = 2L;
    private static final String CODE = "API_ERROR";

    public APIException(String message) {
        super(CODE, message, HttpStatus.BAD_GATEWAY);
    }

    public APIException(String message, Throwable cause) {
        super(CODE, message, cause, HttpStatus.BAD_GATEWAY);
    }

    public APIException(String message, Map<String, Object> details) {
        super(CODE, message, HttpStatus.BAD_GATEWAY, details);
    }

    public APIException(String message, Throwable cause, Map<String, Object> details) {
        super(CODE, message, cause, HttpStatus.BAD_GATEWAY, details);
    }
}
