// InvalidQueryException.java
package com.sawal.exceptions;

/**
 * Thrown when user‐supplied SQL is syntactically invalid or not allowed.
 */
public class InvalidQueryException extends QueryException {
    public InvalidQueryException(String message) {
        super(message);
    }
    public InvalidQueryException(String message, Throwable cause) {
        super(message, cause);
    }
}
