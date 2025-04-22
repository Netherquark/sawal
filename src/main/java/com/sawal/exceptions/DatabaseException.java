// DatabaseException.java
package com.sawal.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import java.util.Map;

/**
 * Thrown for any SQL execution or database connection errors.
 */
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class DatabaseException extends QueryException {
    private static final long serialVersionUID = 3L;
    private static final String CODE = "DB_ERROR";

    public DatabaseException(String message) {
        super(CODE, message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public DatabaseException(String message, Throwable cause) {
        super(CODE, message, cause, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public DatabaseException(String message, Map<String, Object> details) {
        super(CODE, message, HttpStatus.INTERNAL_SERVER_ERROR, details);
    }

    public DatabaseException(String message, Throwable cause, Map<String, Object> details) {
        super(CODE, message, cause, HttpStatus.INTERNAL_SERVER_ERROR, details);
    }
}
