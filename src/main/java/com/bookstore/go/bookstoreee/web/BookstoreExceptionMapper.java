package com.bookstore.go.bookstoreee.web;

import com.bookstore.go.bookstoreee.dto.ErrorBodyDto;
import com.bookstore.go.bookstoreee.exception.BookNotFoundException;
import com.bookstore.go.bookstoreee.exception.BookstoreException;
import com.bookstore.go.bookstoreee.exception.DuplicatedBookException;
import com.bookstore.go.bookstoreee.exception.InvalidBookDataException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

/**
 * Formulate error response.
 */
@Provider
public class BookstoreExceptionMapper implements ExceptionMapper<BookstoreException> {

    @Override
    public Response toResponse(BookstoreException exception) {

        Status status = switch (exception) {
            case BookNotFoundException _ -> Status.NOT_FOUND;
            case DuplicatedBookException _ -> Status.CONFLICT;
            case InvalidBookDataException _ -> Status.BAD_REQUEST;
            default -> Status.INTERNAL_SERVER_ERROR;
        };

        return ErrorBodyDto.of(status, exception.getMessage());
    }
}
