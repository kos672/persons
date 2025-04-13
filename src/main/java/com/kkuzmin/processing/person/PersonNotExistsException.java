package com.kkuzmin.processing.person;

public class PersonNotExistsException extends RuntimeException {

    public PersonNotExistsException(String personId) {
        super("Person with the given id [%s] does not exist.".formatted(personId));
    }
}
