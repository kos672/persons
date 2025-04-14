package com.kkuzmin.processing.person;

import com.kkuzmin.processing.error.ErrorResponse;
import com.kkuzmin.processing.task.TaskResponseDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PersonControllerTest {

    private final String urlPlaceholder = "http://localhost:%s/api/persons";

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PersonRepository personRepository;

    @AfterEach
    public void cleanPersons() {
        this.personRepository.deleteAll();
    }

    @Test
    void upsertPerson_newPersonSupplied() {
        // given
        PersonDTO personDTO = new PersonDTO(null, "Person 1", "Surname 1", LocalDate.of(2005, 4, 14), "Google");

        // when
        ResponseEntity<TaskResponseDTO> createdTaskId = this.restTemplate.postForEntity(urlPlaceholder.formatted(port), personDTO, TaskResponseDTO.class);

        //
        assertThat(createdTaskId).isNotNull();
        assertThat(createdTaskId.getBody()).isNotNull();
        assertThat(createdTaskId.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
    }

    @Test
    @Sql("/person/addPerson.sql")
    void upsertPerson_existingPersonSupplied() {
        // given
        String personId = "9d10e81d-7498-4eb0-90e2-44abe52df116";
        PersonDTO personDTO = new PersonDTO(personId, "Person 2", "Surname 2", LocalDate.of(2005, 4, 14), "Google");

        // when
        ResponseEntity<TaskResponseDTO> createdTaskId = this.restTemplate.postForEntity(urlPlaceholder.formatted(port), personDTO, TaskResponseDTO.class);

        //
        assertThat(createdTaskId).isNotNull();
        assertThat(createdTaskId.getBody()).isNotNull();
        assertThat(createdTaskId.getBody().personId()).isEqualTo(personId);
        assertThat(createdTaskId.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
    }

    @Test
    void upsertPerson_personIdOfNonExistingPersonSupplied() {
        // given
        PersonDTO personDTO = new PersonDTO("03bbe0bb-acfd-4353-9336-837990bc4bba", "Person 1", "Surname 1", LocalDate.of(2005, 4, 14), "Google");

        // when
        ResponseEntity<ErrorResponse> createdTaskId = this.restTemplate.postForEntity(urlPlaceholder.formatted(port), personDTO, ErrorResponse.class);

        //
        assertThat(createdTaskId).isNotNull();
        assertThat(createdTaskId.getBody()).isNotNull();
        assertThat(createdTaskId.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}