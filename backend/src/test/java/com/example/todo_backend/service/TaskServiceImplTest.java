package com.example.todo_backend.service;

import com.example.todo_backend.exception.TaskNotFoundException;
import com.example.todo_backend.model.Task;
import com.example.todo_backend.repository.TaskRepository;
import com.example.todo_backend.service.TaskServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;



@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskServiceImpl taskService;

    private Task task1;
    private Task task2;

    @BeforeEach
    void setUp() {
        task1 = Task.builder()
                .id(1L)
                .title("Task 1")
                .description("Description 1")
                .completed(false)
                .createdAt(LocalDateTime.now())
                .build();

        task2 = Task.builder()
                .id(2L)
                .title("Task 2")
                .description("Description 2")
                .completed(false)
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    void testGetRecentTasks_ReturnsListOfTasks() {
        // Arrange
        List<Task> expectedTasks = Arrays.asList(task1, task2);
        when(taskRepository.findTop5ByCompletedFalseOrderByCreatedAtDesc())
                .thenReturn(expectedTasks);

        // Act
        List<Task> actualTasks = taskService.getRecentTasks();

        // Assert
        assertNotNull(actualTasks);
        assertEquals(2, actualTasks.size());
        assertEquals("Task 1", actualTasks.get(0).getTitle());
        verify(taskRepository, times(1)).findTop5ByCompletedFalseOrderByCreatedAtDesc();
    }

    @Test
    void testGetRecentTasks_ReturnsEmptyList_WhenNoTasks() {
        // Arrange
        when(taskRepository.findTop5ByCompletedFalseOrderByCreatedAtDesc())
                .thenReturn(Arrays.asList());

        // Act
        List<Task> actualTasks = taskService.getRecentTasks();

        // Assert
        assertNotNull(actualTasks);
        assertTrue(actualTasks.isEmpty());
        verify(taskRepository, times(1)).findTop5ByCompletedFalseOrderByCreatedAtDesc();
    }

    @Test
    void testCreateTask_SavesAndReturnsTask() {
        // Arrange
        Task newTask = Task.builder()
                .title("New Task")
                .description("New Description")
                .completed(false)
                .build();

        Task savedTask = Task.builder()
                .id(3L)
                .title("New Task")
                .description("New Description")
                .completed(false)
                .createdAt(LocalDateTime.now())
                .build();

        when(taskRepository.save(any(Task.class))).thenReturn(savedTask);

        // Act
        Task result = taskService.createTask(newTask);

        // Assert
        assertNotNull(result);
        assertEquals(3L, result.getId());
        assertEquals("New Task", result.getTitle());
        assertNotNull(result.getCreatedAt());
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void testMarkTaskAsDone_UpdatesTaskToCompleted() {
        // Arrange
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task1));
        when(taskRepository.save(any(Task.class))).thenReturn(task1);

        // Act
        taskService.markTaskAsDone(1L);

        // Assert
        assertTrue(task1.getCompleted());
        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, times(1)).save(task1);
    }

    @Test
void testMarkTaskAsDone_ThrowsTaskNotFoundException_WhenTaskNotFound() {
    when(taskRepository.findById(999L)).thenReturn(Optional.empty());

    assertThrows(TaskNotFoundException.class, () -> {
        taskService.markTaskAsDone(999L);
    });

    verify(taskRepository, times(1)).findById(999L);
    verify(taskRepository, never()).save(any(Task.class));
}

}