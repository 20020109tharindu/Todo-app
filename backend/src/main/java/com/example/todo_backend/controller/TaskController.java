package com.example.todo_backend.controller;

import com.example.todo_backend.model.Task;
import com.example.todo_backend.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin(origins = {"http://localhost", "http://localhost:80", "http://localhost:5173"}, 
             allowedHeaders = "*",
             methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PATCH, RequestMethod.DELETE})
@RequiredArgsConstructor
public class TaskController {
    
    private final TaskService taskService;

    @GetMapping
    public ResponseEntity<List<Task>> getRecentTasks() {
        return ResponseEntity.ok(taskService.getRecentTasks());
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(taskService.createTask(task));
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<Void> markTaskAsDone(@PathVariable Long id) {
        taskService.markTaskAsDone(id);
        return ResponseEntity.noContent().build();
    }
}