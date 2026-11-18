package com.bookstore.go.bookstoreee.dto;

import java.math.BigDecimal;

/**
 * Dto providing response for total price calculation call.
 */
public record TotalPriceResponseDto(
        BigDecimal totalPrice
) {
}
