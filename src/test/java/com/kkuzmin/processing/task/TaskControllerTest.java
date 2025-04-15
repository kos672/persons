package com.kkuzmin.processing.task;

import com.kkuzmin.processing.field.Classification;
import com.kkuzmin.processing.field.FieldDifferenceDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TaskControllerTest {

    private final String urlPlaceholder = "http://localhost:%s/api/tasks";

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TaskRepository taskRepository;

    @AfterEach
    public void cleanPersons() {
        this.taskRepository.deleteAll();
    }

    @Test
    @Sql("/task/addTask.sql")
    void getTask_existingTaskSuccessfullyRetrieved() {
        // given
        String taskId = "3c28303b-89ad-4002-8c0f-a7716045318e";

        // when
        ResponseEntity<TaskDTO> task = this.restTemplate.getForEntity(urlPlaceholder.formatted(port) + "/" + taskId, TaskDTO.class);

        // then
        assertThat(task).isNotNull();
        assertThat(task.getBody()).isNotNull();
        assertThat(task.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(task.getBody().id()).isEqualTo(taskId);
        assertThat(task.getBody().personId()).isEqualTo("e65f715e-9483-4ae7-a9e8-c4d0502d901f");
        assertThat(task.getBody().progress()).isEqualTo(50);
        assertThat(task.getBody().status()).isEqualTo(TaskStatus.IN_PROGRESS);
        assertThat(task.getBody().results()).hasSize(3);
        assertThat(task.getBody().results().get("45de82d7-c43d-4aad-bfe0-110e5371e3cf")).isEqualTo(new FieldDifferenceDTO("name", null, "Jan", Classification.ADDED, 0));
        assertThat(task.getBody().results().get("12fa507b-940c-4cb1-9ae3-8b97b5d4ed38")).isEqualTo(new FieldDifferenceDTO("surname", null, "Nowak", Classification.ADDED, 0));
        assertThat(task.getBody().results().get("4668c5dd-fb63-46ea-b5b2-6f9879cfb0c7")).isEqualTo(new FieldDifferenceDTO("company", null, "PKP", Classification.ADDED, 0));
    }

    @Test
    void getTask_nonExistingTaskNotRetrieved() {
        // given
        String taskId = "78ee0ab4-52f3-4560-99f7-6412b1cb55cb";

        // when
        ResponseEntity<TaskDTO> task = this.restTemplate.getForEntity(urlPlaceholder.formatted(port) + "/" + taskId, TaskDTO.class);

        // then
        assertThat(task).isNotNull();
        assertThat(task.getBody()).isNull();
        assertThat(task.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @Sql("/task/addThreeTasks.sql")
    void getTasks_allThreeTasksSuccessfullyRetrieved() {
        // given

        // when
        ResponseEntity<List> allTasks = this.restTemplate.getForEntity(urlPlaceholder.formatted(port), List.class);

        // then
        assertThat(allTasks).isNotNull();
        assertThat(allTasks.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(allTasks.getBody()).isNotNull();
        assertThat(allTasks.getBody()).hasSize(3);
    }

    @Test
    void getTasks_noTasksRetrieved() {
        // given

        // when
        ResponseEntity<List> allTasks = this.restTemplate.getForEntity(urlPlaceholder.formatted(port), List.class);

        // then
        assertThat(allTasks).isNotNull();
        assertThat(allTasks.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}