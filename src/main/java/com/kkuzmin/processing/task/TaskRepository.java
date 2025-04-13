package com.kkuzmin.processing.task;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, String> {
}
