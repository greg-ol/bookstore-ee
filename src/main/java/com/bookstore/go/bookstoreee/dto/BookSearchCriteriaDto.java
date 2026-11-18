package com.bookstore.go.bookstoreee.dto;


import com.bookstore.go.bookstoreee.model.BookType;

import java.math.BigDecimal;

/**
 * Dto for providing search criteria.
 */
public record BookSearchCriteriaDto(
        String barcode,
        String name,
        String author,
        Integer quantity,
        BigDecimal price,
        BookType type,
        Integer releaseYear,
        Integer scienceIndex
) {
}
