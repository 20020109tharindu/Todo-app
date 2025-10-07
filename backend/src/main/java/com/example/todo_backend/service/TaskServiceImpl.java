package com.example.todo_backend.service;

import com.example.todo_backend.exception.TaskNotFoundException;
import com.example.todo_backend.model.Task;
import com.example.todo_backend.repository.TaskRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository repository;

    @Override
    public List<Task> getRecentTasks() {
        return repository.findTop5ByCompletedFalseOrderByCreatedAtDesc();
    }

    @Override
    public Task createTask(Task task) {
        return repository.save(task);
    }

    @Override
public void markTaskAsDone(Long id) {
    Task task = repository.findById(id)
            .orElseThrow(() -> new TaskNotFoundException("Task not found with ID: " + id));
    task.setCompleted(true);
    repository.save(task);
}

}
