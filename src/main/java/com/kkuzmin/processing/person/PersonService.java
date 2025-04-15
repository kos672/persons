package com.kkuzmin.processing.person;

import org.springframework.stereotype.Service;

@Service
public class PersonService {

    private final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public PersonDTO getPerson(String id) {
        return id != null ? this.personRepository.findById(id)
                .map(PersonMapper.INSTANCE::toPersonDTO)
                .orElseThrow(() -> new PersonNotExistsException(id)) : null;
    }
}
