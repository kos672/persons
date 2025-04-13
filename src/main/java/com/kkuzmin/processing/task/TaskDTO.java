package com.kkuzmin.processing.task;

import com.kkuzmin.processing.field.FieldDifferenceDTO;

import java.util.Map;

public record TaskDTO(
        String id,
        String personId,
        TaskStatus status,
        int progress,
        Map<String, FieldDifferenceDTO> results
) {}
