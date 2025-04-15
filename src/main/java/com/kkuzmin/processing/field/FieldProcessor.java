package com.kkuzmin.processing.field;

import com.kkuzmin.processing.person.PersonDTO;
import com.kkuzmin.processing.task.Task;
import com.kkuzmin.processing.task.TaskRepository;
import com.kkuzmin.processing.task.TaskStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FieldProcessor {

    private final TaskRepository taskRepository;
    private final FieldDifferenceService fieldDifferenceService;

    @Autowired
    public FieldProcessor(TaskRepository taskRepository, FieldDifferenceService fieldDifferenceService) {
        this.taskRepository = taskRepository;
        this.fieldDifferenceService = fieldDifferenceService;
    }

    @Async
    public void processFields(PersonDTO newPerson, PersonDTO previousPerson, Task task) {
        try {
            task.setStatus(TaskStatus.IN_PROGRESS);
            task.setProgress(10);
            taskRepository.save(task);

            processField("name", previousPerson, newPerson)
                    .ifPresent(diff -> task.putResult("name", diff));
            task.setProgress(25);
            taskRepository.save(task);

            processField("surname", previousPerson, newPerson)
                    .ifPresent(diff -> task.putResult("surname", diff));
            task.setProgress(50);
            taskRepository.save(task);

            processField("company", previousPerson, newPerson)
                    .ifPresent(diff -> task.putResult("company", diff));
            task.setProgress(75);
            taskRepository.save(task);

            task.setProgress(100);
            task.setStatus(TaskStatus.COMPLETED);
            taskRepository.save(task);
        } catch (Exception e) {
            task.setStatus(TaskStatus.FAILED);
            taskRepository.save(task);
            throw new FieldProcessingException(e);
        }
    }

    private Optional<FieldDifference> processField(String fieldName, PersonDTO previousPerson, PersonDTO newPerson) throws InterruptedException {
        String prevValue = getFieldValue(previousPerson, fieldName);
        String newValue = getFieldValue(newPerson, fieldName);
        Thread.sleep(3000);
        return fieldDifferenceService.calculateDifference(fieldName, prevValue, newValue);
    }

    private String getFieldValue(PersonDTO person, String fieldName) {
        if (person == null) {
            return null;
        }

        return switch (fieldName) {
            case "name" -> person.name();
            case "surname" -> person.surname();
            case "company" -> person.company();
            default -> null;
        };
    }

}
