// DatabaseException.java
package com.sawal.exceptions;

/**
 * Thrown when there is any problem connecting to or querying the database.
 */
public class DatabaseException extends RuntimeException {
    public DatabaseException(String message) {
        super(message);
    }
    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
