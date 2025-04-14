package com.kkuzmin.processing.task;

import com.kkuzmin.processing.field.FieldDifference;
import com.kkuzmin.processing.field.FieldProcessor;
import com.kkuzmin.processing.person.Person;
import com.kkuzmin.processing.person.PersonDTO;
import com.kkuzmin.processing.person.PersonMapper;
import com.kkuzmin.processing.person.PersonNotExistsException;
import com.kkuzmin.processing.person.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final FieldProcessor fieldProcessor;
    private final PersonRepository personRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository, FieldProcessor fieldProcessor, PersonRepository personRepository) {
        this.taskRepository = taskRepository;
        this.fieldProcessor = fieldProcessor;
        this.personRepository = personRepository;
    }

    @Transactional
    public Task createTask(PersonDTO personDTO) {
        String personId = personDTO.id();
        if (personId != null && !personRepository.existsById(personId)) {
            throw new PersonNotExistsException(personId);
        }
        Person person = PersonMapper.INSTANCE.toPersonEntity(personDTO);
        personRepository.save(person);

        Task task = new Task(person.getId());
        taskRepository.save(task);

        return task;
    }

    public CompletableFuture<Map<String, FieldDifference>> executeTask(PersonDTO newPersonDTO, Task task) {
        String personId = task.getPersonId();
        Person previousPerson = personRepository.findById(personId).orElseThrow(() -> new PersonNotExistsException(personId));
        PersonDTO previousPersonDTO = PersonMapper.INSTANCE.toPersonDTO(previousPerson);
        return fieldProcessor.processFields(newPersonDTO, previousPersonDTO, task);
    }

    public List<TaskDTO> getAllTasks() {
        return taskRepository.findAll().stream()
                .map(TaskMapper.INSTANCE::toTaskDTO)
                .toList();
    }

    @Cacheable(value = "tasks", unless = "#result.progress<100")
    public Optional<TaskDTO> getTask(String taskId) {
        return taskRepository.findById(taskId)
                .map(TaskMapper.INSTANCE::toTaskDTO);
    }


}
