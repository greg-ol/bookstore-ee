package com.bookstore.go.bookstoreee.exception;

/**
 * Exception for invalid book data when creating or updating book.
 */
public class InvalidBookDataException extends BookstoreException {
    public InvalidBookDataException(String message) {
        super(message);
    }
}
