package com.bookstore.go.bookstoreee.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

/**
 * Dto for updating book record.
 */
public class BookUpdateRequestDto {
    private String name;
    private String author;
    @PositiveOrZero
    private Integer quantity;
    @DecimalMin("0.00")
    @Digits(integer = 4, fraction = 2)
    private BigDecimal price;
    private Integer releaseYear;
    @Min(1)
    @Max(10)
    private Integer scienceIndex;

    public BookUpdateRequestDto() {
    }

    public BookUpdateRequestDto(String name, String author, Integer quantity, BigDecimal price, Integer releaseYear, Integer scienceIndex) {
        this.name = name;
        this.author = author;
        this.quantity = quantity;
        this.price = price;
        this.releaseYear = releaseYear;
        this.scienceIndex = scienceIndex;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(Integer releaseYear) {
        this.releaseYear = releaseYear;
    }

    public Integer getScienceIndex() {
        return scienceIndex;
    }

    public void setScienceIndex(Integer scienceIndex) {
        this.scienceIndex = scienceIndex;
    }
}
