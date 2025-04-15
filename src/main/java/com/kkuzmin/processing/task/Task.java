package com.kkuzmin.processing.task;

import com.kkuzmin.processing.field.FieldDifference;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MapKeyJoinColumn;
import jakarta.persistence.Table;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "TASK")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "PERSON_ID")
    private String personId;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private TaskStatus status;

    @Column(name = "PROGRESS")
    private int progress;

    @ElementCollection
    @CollectionTable(name = "task_results")
    @MapKeyJoinColumn(name = "field_name")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Map<String, FieldDifference> results;

    public Task() {
    }

    public Task(String personId) {
        this.personId = personId;
        this.status = TaskStatus.PENDING;
        this.progress = 0;
        this.results = new HashMap<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public Map<String, FieldDifference> getResults() {
        return results;
    }

    public void setResults(Map<String, FieldDifference> results) {
        this.results = results;
    }

    public void putResult(String key, FieldDifference result) {
        this.results.put(key, result);
    }
}
