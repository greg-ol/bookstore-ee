package com.bookstore.go.bookstoreee.exception;

/**
 * Exception for duplicated books (barcode has to be unique).
 */
public class DuplicatedBookException extends BookstoreException {

    public DuplicatedBookException(String barcode) {
        super("Book with barcode: " + barcode + " already exists");
    }
}
