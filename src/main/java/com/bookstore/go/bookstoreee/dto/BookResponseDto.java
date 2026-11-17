package com.bookstore.go.bookstoreee.dto;


import com.bookstore.go.bookstoreee.model.Book;
import com.bookstore.go.bookstoreee.model.BookType;

import java.math.BigDecimal;

/**
 * Dto for transportation of book response
 */
public class BookResponseDto {

    private String name;
    private String author;
    private String barcode;
    private int quantity;
    private BigDecimal price;
    private BookType type;
    private Integer releaseYear;
    private Integer scienceIndex;

    public BookResponseDto() {
    }

    public BookResponseDto(String name, String author, String barcode, int quantity, BigDecimal price, BookType type, Integer releaseYear, Integer scienceIndex) {
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
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

    public static BookResponseDto from(Book b) {
        return new BookResponseDto(b.getName(), b.getAuthor(), b.getBarcode(), b.getQuantity(), b.getPrice()
                , b.getType(), b.getReleaseYear(), b.getScienceIndex());
    }
}
