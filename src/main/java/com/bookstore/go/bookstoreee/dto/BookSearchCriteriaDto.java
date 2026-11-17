package com.bookstore.go.bookstoreee.dto;


import com.bookstore.go.bookstoreee.model.BookType;

import java.math.BigDecimal;

/**
 * Dto for providing search criteria.
 */
public class BookSearchCriteriaDto {
    private String barcode;
    private String name;
    private String author;
    private Integer quantity;
    private BigDecimal price;
    private BookType type;
    private Integer releaseYear;
    private Integer scienceIndex;

    public BookSearchCriteriaDto() {
    }

    public BookSearchCriteriaDto(String barcode, String name, String author, Integer quantity, BigDecimal price, BookType type, Integer releaseYear, Integer scienceIndex) {
        this.barcode = barcode;
        this.name = name;
        this.author = author;
        this.quantity = quantity;
        this.price = price;
        this.type = type;
        this.releaseYear = releaseYear;
        this.scienceIndex = scienceIndex;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
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
