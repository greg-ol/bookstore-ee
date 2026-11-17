package com.bookstore.go.bookstoreee.dto;

import com.bookstore.go.bookstoreee.model.BookType;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

/**
 * Dto for passing Book data in request.
 */
public class BookRequestDto {
    @NotBlank
    private String name;

    @NotBlank
    private String author;

    @NotBlank
    private String barcode;

    @NotNull
    @PositiveOrZero
    private Integer quantity;

    @NotNull
    @DecimalMin("0.00")
    @Digits(integer = 4, fraction = 2)
    private BigDecimal price;

    private BookType type;

    @Max(1900)
    private Integer releaseYear;

    @Min(1)
    @Max(10)
    private Integer scienceIndex;

    public BookRequestDto() {
    }

    public BookRequestDto(String name, String author, String barcode, Integer quantity, BigDecimal price, BookType type, Integer releaseYear, Integer scienceIndex) {
        this.name = name;
        this.author = author;
        this.barcode = barcode;
        this.quantity = quantity;
        this.price = price;
        this.type = type;
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

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
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

    public BookType getType() {
        return type;
    }

    public void setType(BookType type) {
        this.type = type;
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
