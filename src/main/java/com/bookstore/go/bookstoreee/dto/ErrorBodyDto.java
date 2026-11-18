package com.bookstore.go.bookstoreee.dto;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Dto for passing error information.
 */
public class ErrorBodyDto {

    private ErrorBodyDto() {
    }

    public static Response of(Response.Status status, String message) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status", status.getStatusCode());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);

        return Response.status(status).type(MediaType.APPLICATION_JSON).entity(body).build();
    }
}
