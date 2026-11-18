package com.bookstore.go.bookstoreee.exception;

/**
 * General bookstore exception - only for purpose of error mapping.
 */
public abstract class BookstoreException extends RuntimeException {

    protected BookstoreException(String message) {
        super(message);
    }
}
