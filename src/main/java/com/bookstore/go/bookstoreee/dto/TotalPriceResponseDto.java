package com.bookstore.go.bookstoreee.dto;

import java.math.BigDecimal;

/**
 * Dto providing response for total price calculation call.
 */
public class TotalPriceResponseDto {

    private BigDecimal totalPrice;

    public TotalPriceResponseDto() {
    }

    public TotalPriceResponseDto(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
}
