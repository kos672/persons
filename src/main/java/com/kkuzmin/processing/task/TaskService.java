package com.kkuzmin.processing.task;

import com.kkuzmin.processing.field.FieldProcessor;
import com.kkuzmin.processing.person.Person;
import com.kkuzmin.processing.person.PersonDTO;
import com.kkuzmin.processing.person.PersonMapper;
import com.kkuzmin.processing.person.PersonNotExistsException;
import com.kkuzmin.processing.person.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public Task processPersonUpdate(Person person) {
        Person previousPerson = person.getId() != null ? personRepository.findById(person.getId()).orElseThrow(() -> new PersonNotExistsException(person.getId())) : null;

        PersonDTO newPersonDTO = PersonMapper.INSTANCE.toPersonDTO(person);
        PersonDTO previousPersonDTO = PersonMapper.INSTANCE.toPersonDTO(previousPerson);

        personRepository.save(person);

        Task task = new Task(person.getId());
        taskRepository.save(task);

        fieldProcessor.processFields(newPersonDTO, previousPersonDTO, task);
        return task;
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
