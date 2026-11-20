package com.bookstore.go.bookstoreee.service;

import com.bookstore.go.bookstoreee.dto.BookRequestDto;
import com.bookstore.go.bookstoreee.dto.BookResponseDto;
import com.bookstore.go.bookstoreee.dto.BookSearchCriteriaDto;
import com.bookstore.go.bookstoreee.dto.BookUpdateRequestDto;
import com.bookstore.go.bookstoreee.exception.BookNotFoundException;
import com.bookstore.go.bookstoreee.exception.DuplicatedBookException;
import com.bookstore.go.bookstoreee.exception.InvalidBookDataException;
import com.bookstore.go.bookstoreee.model.BookType;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BookServiceTest {

    private static EntityManagerFactory emf;
    private static BookService service;

    @BeforeAll
    static void beforeAll() {
        emf = Persistence.createEntityManagerFactory("bookstorePU",
                Map.of("jakarta.persistence.jdbc.url", "jdbc:h2:mem:servicetest;DB_CLOSE_DELAY=-1"));

        Validator validator = Validation.byDefaultProvider().configure()
                .messageInterpolator(new ParameterMessageInterpolator())
                .buildValidatorFactory().getValidator();

        service = new BookService(emf, validator, new BookPriceHelper());
    }

    @AfterAll
    static void close() {
        emf.close();
    }

    @Test
    void createGetByBarcodeAndUpdate() {
        service.add(getRequest("B-1", 2, "20.00"));
        BookResponseDto b1 = service.get("B-1");

        assertEquals("B-1", b1.barcode());
        assertEquals("NameB-1", b1.name());

        BookResponseDto b2 = service.update("B-1", new BookUpdateRequestDto("UpdatedName", null, null, null, null, null));

        assertEquals("UpdatedName", b2.name());
        assertEquals("B-1", b1.barcode());
    }

    @Test
    void duplicatedBarcodeRejected() {
        service.add(getRequest("B-2", 2, "20.00"));
        assertThrows(DuplicatedBookException.class, () -> service.add(getRequest("B-2", 2, "20.00")));
    }

    @Test
    void unknownBarcode() {
        assertThrows(BookNotFoundException.class, () -> service.get("unknown barcode"));
    }

    @Test
    void searchBySubsetOfAttributes() {
        BookResponseDto response = service.add(new BookRequestDto("Dune", "Frank Herbert", "T-5", 1, new BigDecimal("9.99"), BookType.STANDARD, null, null));
        BookSearchCriteriaDto c = new BookSearchCriteriaDto(null, null, "Frank Herbert", null, null, null, null, null);
        List<BookResponseDto> found = service.search(c);
        assertEquals(1, found.size());
    }

    @Test
    void importIsAllOrNothing() {
        var bad = new BookRequestDto("I-2", "Bad", "Me", 1, new BigDecimal("1"), BookType.SCIENCE_JOURNAL, null, null);
        assertThrows(InvalidBookDataException.class, () -> service.importBooks(List.of(getRequest("I-1", 1, "1.00"), bad)));
        assertThrows(BookNotFoundException.class, () -> service.get("I-1"));
    }


    private static BookRequestDto getRequest(String barcode, int qty, String price) {
        return new BookRequestDto("Name" + barcode, "Author", barcode, qty, new BigDecimal(price), null, null, null);
    }

}