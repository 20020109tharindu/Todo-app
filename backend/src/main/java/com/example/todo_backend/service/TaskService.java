package com.example.todo_backend.service;

import com.example.todo_backend.model.Task;
import java.util.List;

public interface TaskService {
    List<Task> getRecentTasks();
    Task createTask(Task task);
    void markTaskAsDone(Long id);
}
