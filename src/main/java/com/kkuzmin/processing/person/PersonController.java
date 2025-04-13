package com.kkuzmin.processing.person;

import com.kkuzmin.processing.task.Task;
import com.kkuzmin.processing.task.TaskResponseDTO;
import com.kkuzmin.processing.task.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/persons")
@Tag(name = "Persons")
public class PersonController {

    private final TaskService taskService;

    @Autowired
    public PersonController(TaskService taskService) {
        this.taskService = taskService;
    }

    @Operation(summary = "Upsert a person", description = "Create a person if does not exist, otherwise update existing one.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "A person was successfully created. " +
                    "A task responsible for calculation of similarities across person's field successfully submitted, which runs in background. " +
                    "The current status of the task can be tracked via api/tasks endpoint."),
            @ApiResponse(responseCode = "400", description = "There is no existing person to the supplied personId.")
    })
    @PostMapping
    public ResponseEntity<TaskResponseDTO> upsertPerson(@RequestBody PersonDTO personDTO) {
        Person person = PersonMapper.INSTANCE.toPersonEntity(personDTO);
        Task task = taskService.processPersonUpdate(person);
        return ResponseEntity.accepted().body(new TaskResponseDTO(task.getId(), person.getId(), task.getStatus(), task.getProgress()));
    }

}


