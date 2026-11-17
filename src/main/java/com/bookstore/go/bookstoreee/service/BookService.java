package com.bookstore.go.bookstoreee.service;

import com.bookstore.go.bookstoreee.dto.BookRequestDto;
import com.bookstore.go.bookstoreee.dto.BookResponseDto;
import com.bookstore.go.bookstoreee.exception.DuplicatedBookException;
import com.bookstore.go.bookstoreee.exception.InvalidBookDataException;
import com.bookstore.go.bookstoreee.model.Book;
import com.bookstore.go.bookstoreee.model.BookType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Service implementing business logic for bookstore.
 */
public class BookService {

    private static final int MAX_YEAR_ANTIQUE = 1900;

    private final EntityManagerFactory factory;
    private final Validator validator;

    public BookService(EntityManagerFactory factory, Validator validator) {
        this.factory = factory;
        this.validator = validator;
    }

    public BookResponseDto add(BookRequestDto request) {

        validateRequest(request);
        String barcode = request.getBarcode().trim();
     return   inTransaction(em -> {
            if (findBook(em, barcode).isPresent()) {
                throw new DuplicatedBookException(barcode);
            }

            Book book = new Book();
            book.setBarcode(barcode);
            populateBook(book, request);

            em.persist(book);
            em.flush();
            return convertToResponse(book);
        });
    }

    private <T> T inTransaction(Function<EntityManager, T> work) {
        EntityManager em = factory.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            T result = work.apply(em);
            tx.commit();
            return result;
        } catch (RuntimeException ex) {
            if (tx.isActive()) {
                tx.rollback();
            }
             throw  ex;
        } finally {
            em.close();
        }
    }

    private <T> void validateRequest(T dto) {
        if(dto == null) throw  new InvalidBookDataException("Request body is required");
        Set<ConstraintViolation<T>> violations = validator.validate(dto);

        if(!violations.isEmpty()) {
            String msg = violations.stream()
                    .map(v -> v.getPropertyPath() + " " +
                            v.getMessage())
                    .sorted().collect(Collectors.joining("; "));
     throw  new InvalidBookDataException(msg);
        }
    }

    private BookResponseDto convertToResponse(Book b) {
        return BookResponseDto.from(b);
    }

    private void populateBook(Book book, BookRequestDto dto) {
        book.setName(dto.getName());
        book.setAuthor(dto.getAuthor());
        book.setQuantity(dto.getQuantity()));
        book.setPrice(dto.getPrice());
        book.setType(dto.getType() == null ? BookType.STANDARD : dto.getType());
        book.setReleaseYear(dto.getReleaseYear());
        book.setScienceIndex(dto.getScienceIndex());
        checkValidity(book);
    }

    private void checkValidity(Book b) {
        switch (b.getType()) {
            case ANTIQUE -> {
                checkReleaseYaar(b);
                if (b.getReleaseYear() <= 0 || b.getReleaseYear() > MAX_YEAR_ANTIQUE) {
                    report("release year must be between 1 and 1900");
                }
                checkScienceIndex(b);

            }
            case STANDARD -> {
                checkScienceIndex(b);
                if (b.getScienceIndex() != null) {
                    report("science index only required for SCIENCE_JOURNAL");
                }
            }
            case SCIENCE_JOURNAL -> {
                checkReleaseYaar(b);
                if (b.getScienceIndex() == null || b.getScienceIndex() < 1 || b.getScienceIndex() > 10) {
                    report("science index should have values 1-10");
                }
            }
        }
    }

    private static void checkReleaseYaar(Book b) {
        if (b.getReleaseYear() == null && b.getType() == BookType.ANTIQUE) {
            report("release year required for ANTIQUE book");
        }

        if (b.getReleaseYear() != null && b.getType() != BookType.ANTIQUE) {
            {
                report("release year should be provided only for ANTIQUE book");
            }
        }
    }

    private static void checkScienceIndex(Book b) {
        if (b.getScienceIndex() != null) {
            report("science index not allowed for Book Type: " + b.getType());
        }
    }


    private static void report(String message) {
        throw new InvalidBookDataException(message);
    }

    private Optional<Book> findBook(EntityManager em, String barcode) {
        return em.createQuery("select b from Book b where b.barcode = :barcode", Book.class)
                .setParameter("barcode", barcode).getResultStream().findFirst();
    }
}
