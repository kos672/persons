package com.kkuzmin.processing.task;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@Tag(name = "Tasks")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @Operation(summary = "Get all tasks", description = "Get all tasks including fields' difference data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tasks found"),
            @ApiResponse(responseCode = "404", description = "No tasks found"),
    })
    @GetMapping
    public ResponseEntity<List<TaskDTO>> listTasks() {
        List<TaskDTO> tasks = taskService.getAllTasks();
        return tasks.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(tasks);
    }

    @Operation(summary = "Get task", description = "Get a task for the given taskId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task was found"),
            @ApiResponse(responseCode = "404", description = "Task for the supplied taskId could not be found."),
    })
    @GetMapping("/{taskId}")
    public ResponseEntity<TaskDTO> getTaskStatus(@PathVariable(name = "taskId") String taskId) {
        return taskService.getTask(taskId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
