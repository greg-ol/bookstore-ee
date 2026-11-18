package com.bookstore.go.bookstoreee.service;

import com.bookstore.go.bookstoreee.model.Book;
import com.bookstore.go.bookstoreee.model.BookType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class BookPriceHelperTest {

    private final BookPriceHelper helper = new BookPriceHelper();

    private final LocalDate now = LocalDate.of(2026, 10, 10);

    private Book createBook(BookType type, int qty, String price, int releaseYear, int scienceIndex) {
        Book b = new Book();
        b.setType(type);
        b.setQuantity(qty);
        b.setPrice(new BigDecimal(price));
        b.setReleaseYear(releaseYear);
        b.setScienceIndex(scienceIndex);

        return b;
    }

    @Test
    void checkStdBook() {
        try (MockedStatic<LocalDate> mocked = Mockito.mockStatic(LocalDate.class)) {
            mocked.when(LocalDate::now).thenReturn(now);

            Book b = createBook(BookType.STANDARD, 10, "29.99", 1900, 10);
            BigDecimal price = helper.totalPrice(b);
            assertEquals(0, new BigDecimal("299.90").compareTo(price));
        }
    }

    @Test
    void checkAntiqueBook() {
        try (MockedStatic<LocalDate> mocked = Mockito.mockStatic(LocalDate.class)) {
            mocked.when(LocalDate::now).thenReturn(now);

            Book b = createBook(BookType.ANTIQUE, 2, "100.00", 1800, 10);
            BigDecimal price = helper.totalPrice(b);
            assertEquals(0, new BigDecimal("4520.00").compareTo(price));
        }
    }

    @Test
    void checkScienceJournal() {
        try (MockedStatic<LocalDate> mocked = Mockito.mockStatic(LocalDate.class)) {
            mocked.when(LocalDate::now).thenReturn(now);

            Book b = createBook(BookType.SCIENCE_JOURNAL, 3, "10.50", 1900, 4);
            BigDecimal price = helper.totalPrice(b);
            assertEquals(0, new BigDecimal("126.00").compareTo(price));
        }
    }

}