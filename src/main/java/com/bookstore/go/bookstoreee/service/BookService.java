package com.bookstore.go.bookstoreee.service;

import com.bookstore.go.bookstoreee.dto.BookRequestDto;
import com.bookstore.go.bookstoreee.dto.BookResponseDto;
import jakarta.persistence.EntityManagerFactory;

/**
 * Service implementing business logic for bookstore.
 */
public class BookService {

    private final EntityManagerFactory factory;

    public BookService(EntityManagerFactory factory) {
        this.factory = factory;
    }

    public BookResponseDto add(BookRequestDto request) {
    }
}
