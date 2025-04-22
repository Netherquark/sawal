// APIException.java
package com.sawal.exceptions;

/**
 * A generic runtime exception for wrapping any API‐layer errors.
 */
public class APIException extends RuntimeException {
    public APIException(String message) {
        super(message);
    }
    public APIException(String message, Throwable cause) {
        super(message, cause);
    }
}
