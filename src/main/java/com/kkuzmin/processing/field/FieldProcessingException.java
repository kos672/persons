package com.kkuzmin.processing.field;

public class FieldProcessingException extends RuntimeException {

    public FieldProcessingException(Exception e) {
        super("Field processing failed due to the following reason: %s".formatted(e.getMessage()));
    }
}
