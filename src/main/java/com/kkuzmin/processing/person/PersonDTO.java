package com.kkuzmin.processing.person;

import java.time.LocalDate;

public record PersonDTO(
        String id,
        String name,
        String surname,
        LocalDate birthDate,
        String company
) {}
