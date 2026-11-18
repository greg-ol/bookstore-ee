package com.bookstore.go.bookstoreee.web;

import com.bookstore.go.bookstoreee.dto.*;
import com.bookstore.go.bookstoreee.service.BookService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;
import java.util.Map;

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

    @GET
    @Path("/{barcode}")
    public BookResponseDto get(@PathParam("barcode") String barcode) {
        return service.get(barcode);
    }

    @POST
    @Path("/search")
    public List<BookResponseDto> search(BookSearchCriteriaDto searchCriteria) {
        return service.search(searchCriteria);
    }

    @PATCH
    @Path("/{barcode}")
    public BookResponseDto update(@PathParam("barcode") String barcode, BookUpdateRequestDto request) {
        return service.update(barcode, request);
    }

    @GET
    @Path("/total-price")
    public TotalPriceResponseDto getTotalPrice(@QueryParam("barcodes") List<String> barcodes) {
        return service.getTotalPrice(barcodes);
    }

    @GET
    @Path("/barcodes-by-quantity")
    public Map<Integer, List<String>> getBarcodesByQuantity() {
        return service.getBarcodesByQuantity();
    }

    @GET
    @Path("/export")
    public List<BookResponseDto> export() {
        return service.export();
    }

    @POST
    @Path("/import")
    public ImportResultDto importBooks(List<BookRequestDto> books) {
        return service.importBooks(books);
    }
}
