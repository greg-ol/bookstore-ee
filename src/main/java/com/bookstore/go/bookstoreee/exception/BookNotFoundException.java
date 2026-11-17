package com.bookstore.go.bookstoreee.exception;

/**
 * Book not found exception.
 */
public class BookNotFoundException extends RuntimeException {

    public BookNotFoundException(String message) {
        super(message);
    }
}
