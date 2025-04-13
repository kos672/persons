package com.kkuzmin.processing.error;

import java.time.LocalDateTime;

public record ErrorResponse(
        String message,
        String details,
        LocalDateTime occurred
) { }
