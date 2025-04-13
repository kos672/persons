package com.kkuzmin.processing.task;

public record TaskResponseDTO(
        String taskId,
        String personId,
        TaskStatus status,
        int progress
) {}
