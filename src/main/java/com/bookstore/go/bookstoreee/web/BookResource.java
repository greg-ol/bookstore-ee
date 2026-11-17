package com.bookstore.go.bookstoreee.web;

import com.bookstore.go.bookstoreee.dto.BookRequestDto;
import com.bookstore.go.bookstoreee.dto.BookResponseDto;
import com.bookstore.go.bookstoreee.service.BookService;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/books")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookResource {

    @Inject
    BookService service;

    @POST
    public BookResponseDto add(BookRequestDto request) {
        return service.add(request);
    }
}
