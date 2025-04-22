// QueryException.java
package com.sawal.exceptions;

/**
 * Base class for anything going wrong during SQL construction or execution.
 */
public class QueryException extends RuntimeException {
    public QueryException(String message) {
        super(message);
    }
    public QueryException(String message, Throwable cause) {
        super(message, cause);
    }
}
