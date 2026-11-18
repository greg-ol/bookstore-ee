package com.bookstore.go.bookstoreee.service;

import com.bookstore.go.bookstoreee.dto.*;
import com.bookstore.go.bookstoreee.exception.BookNotFoundException;
import com.bookstore.go.bookstoreee.exception.DuplicatedBookException;
import com.bookstore.go.bookstoreee.exception.InvalidBookDataException;
import com.bookstore.go.bookstoreee.model.Book;
import com.bookstore.go.bookstoreee.model.BookType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Service implementing business logic for bookstore.
 */
public class BookService {

    private static final int MAX_YEAR_ANTIQUE = 1900;

    private final EntityManagerFactory factory;
    private final Validator validator;
    private final BookPriceHelper helper;

    public BookService(EntityManagerFactory factory, Validator validator, BookPriceHelper helper) {
        this.factory = factory;
        this.validator = validator;
        this.helper = helper;
    }

    public BookResponseDto add(BookRequestDto request) {

        validateRequest(request);
        String barcode = request.barcode().trim();
        return inTransaction(em -> {
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

    public BookResponseDto get(String barcode) {
        return inTransaction(em -> convertToResponse(getBook(em, barcode)));
    }

    public BookResponseDto update(String barcode, BookUpdateRequestDto request) {
        if (request == null) throw new InvalidBookDataException("Request body must be provided");
        validateRequest(request);
        return inTransaction(em -> {
            Book b = getBook(em, barcode);
            if (request.name() != null) b.setName(request.name());
            if (!request.author().isBlank()) b.setAuthor(request.author());
            if (request.quantity() != null) b.setQuantity(request.quantity());
            if (request.price() != null) b.setPrice(request.price());
            if (request.releaseYear() != null) b.setReleaseYear(request.releaseYear());
            if (request.scienceIndex() != null) b.setScienceIndex(request.scienceIndex());

            checkValidity(b);
            return convertToResponse(b);
        });
    }

    public List<BookResponseDto> search(BookSearchCriteriaDto dto) {
        return inTransaction(em -> {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Book> quary = cb.createQuery(Book.class);
            Root<Book> root = quary.from(Book.class);
            List<Predicate> predicates = new ArrayList<>();
            if (!dto.barcode().isBlank()) {
                predicates.add(cb.equal(root.get("barcode"), dto.barcode()));
            }
            if (!dto.name().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("name")), dto.name()));
            }
            if (!dto.author().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("author")), dto.author()));
            }
            if (dto.quantity() != null) {
                predicates.add(cb.equal(root.get("quantity"), dto.quantity()));
            }
            if (dto.price() != null) {
                predicates.add(cb.equal(root.get("price"), dto.price()));
            }
            if (dto.type() != null) {
                predicates.add(cb.equal(root.get("type"), dto.type()));
            }
            if (dto.releaseYear() != null) {
                predicates.add(cb.equal(root.get("releaseYear"), dto.releaseYear()));
            }
            if (dto.scienceIndex() != null) {
                predicates.add(cb.equal(root.get("scienceIdex"), dto.scienceIndex()));
            }

            quary.select(root).where(predicates.toArray(new Predicate[0])).orderBy(cb.asc(root.get("barcode")));

            return em.createQuery(quary).getResultList().stream().map(this::convertToResponse).toList();
        });
    }

    public TotalPriceResponseDto getTotalPrice(List<String> barcodes) {
        Set<String> requestBarcodes = new HashSet<>(barcodes);

        return inTransaction(em -> {
            BigDecimal sum = BigDecimal.valueOf(0);
            for (String bc : requestBarcodes) {
                Book b = getBook(em, bc);
                BigDecimal price = this.helper.totalPrice(b);
                sum = sum.add(price);
            }

            return new TotalPriceResponseDto(sum);
        });
    }

    public Map<Integer, List<String>> getBarcodesByQuantity() {
        Comparator<Book> compByTotal = Comparator.comparing(helper::totalPrice)
                .thenComparing(Book::getBarcode);

        return inTransaction(em -> {
            // all books with quantity bigger then 0

            List<Book> books = em.createQuery("select b from Book b where  b.quantity > 0", Book.class).getResultList()
                    .stream().filter(b -> b.getQuantity() > 0).toList();

            return books
                    .stream().collect(Collectors.groupingBy(Book::getQuantity, TreeMap::new,
                            Collectors.collectingAndThen(Collectors.toList(),
                                    list -> list.stream().sorted(compByTotal).map(Book::getBarcode).toList())));

        });
    }

    public List<BookResponseDto> export() {
        return inTransaction(em ->
                em.createQuery("select b from Book b order by b.barcode", Book.class).getResultList().stream()
                        .map(BookResponseDto::from).toList());
    }

    public ImportResultDto importBooks(List<BookRequestDto> books) {
        if (books == null) throw new InvalidBookDataException("Request body must not be null");

        return inTransaction(em -> {

            AtomicInteger createdRecords = new AtomicInteger();
            books.forEach(b -> {
                String barcode = b.barcode();
                Optional<Book> existingBook = findBook(em, barcode);

                // avoid duplications
                if (existingBook.isPresent()) {
                    throw new DuplicatedBookException(barcode);
                }

                Book record = new Book();
                record.setBarcode(barcode);
                populateBook(record, b);
                checkValidity(record);

                em.persist(record);
                createdRecords.getAndIncrement();
            });

            return new ImportResultDto(createdRecords.get());
        });
    }


    private Book getBook(EntityManager em, String barcode) {
        return findBook(em, barcode).orElseThrow(() -> new BookNotFoundException("Not found book with barcode: " + barcode));
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
            throw ex;
        } finally {
            em.close();
        }
    }

    private <T> void validateRequest(T dto) {
        if (dto == null) throw new InvalidBookDataException("Request body is required");
        Set<ConstraintViolation<T>> violations = validator.validate(dto);

        if (!violations.isEmpty()) {
            String msg = violations.stream()
                    .map(v -> v.getPropertyPath() + " " +
                            v.getMessage())
                    .sorted().collect(Collectors.joining("; "));
            throw new InvalidBookDataException(msg);
        }
    }

    private BookResponseDto convertToResponse(Book b) {
        return BookResponseDto.from(b);
    }

    private void populateBook(Book book, BookRequestDto dto) {
        book.setName(dto.name());
        book.setAuthor(dto.author());
        book.setQuantity(dto.quantity());
        book.setPrice(dto.price());
        book.setType(dto.type() == null ? BookType.STANDARD : dto.type());
        book.setReleaseYear(dto.releaseYear());
        book.setScienceIndex(dto.scienceIndex());
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
