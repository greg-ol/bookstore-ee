package com.bookstore.go.bookstoreee.dto;

/**
 * Dto for passing result of import operation.
 */
public class ImportResultDto {

    private int createdRecords;

    public ImportResultDto() {

    }

    public ImportResultDto(int createdRecords) {
        this.createdRecords = createdRecords;
    }

    public int getCreatedRecords() {
        return createdRecords;
    }

    public void setCreatedRecords(int createdRecords) {
        this.createdRecords = createdRecords;
    }
}
