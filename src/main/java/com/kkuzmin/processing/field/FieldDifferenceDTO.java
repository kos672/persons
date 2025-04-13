package com.kkuzmin.processing.field;

public record FieldDifferenceDTO(
        String fieldName,
        String previousValue,
        String newValue,
        Classification classification,
        double similarity
) {}
