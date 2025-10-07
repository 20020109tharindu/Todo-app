package com.example.todo_backend.repository;

import com.example.todo_backend.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test") // ensures we use application-test.properties with H2
class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    @BeforeEach
    void setUp() {
        // Clean up before each test to avoid conflicts
        taskRepository.deleteAll();
    }

    @Test
    void testFindTop5ByCompletedFalseOrderByCreatedAtDesc_ReturnsRecentTasks() {
        // Arrange - create 7 incomplete tasks
        for (int i = 1; i <= 7; i++) {
            Task task = Task.builder()
                    .title("Task " + i)
                    .description("Description " + i)
                    .completed(false)
                    .build();
            taskRepository.save(task);
        }

        // Act - fetch top 5 recent incomplete tasks
        List<Task> recentTasks = taskRepository.findTop5ByCompletedFalseOrderByCreatedAtDesc();

        // Assert - verify only 5 and ordered correctly
        assertEquals(5, recentTasks.size());
        assertEquals("Task 7", recentTasks.get(0).getTitle());
    }

    @Test
    void testFindTop5ByCompletedFalseOrderByCreatedAtDesc_ExcludesCompletedTasks() {
        // Arrange - add one completed and one incomplete task
        Task completedTask = Task.builder()
                .title("Completed Task")
                .description("Done")
                .completed(true)
                .build();
        taskRepository.save(completedTask);

        Task activeTask = Task.builder()
                .title("Active Task")
                .description("Not done")
                .completed(false)
                .build();
        taskRepository.save(activeTask);

        // Act
        List<Task> recentTasks = taskRepository.findTop5ByCompletedFalseOrderByCreatedAtDesc();

        // Assert - only active task should appear
        assertEquals(1, recentTasks.size());
        assertEquals("Active Task", recentTasks.get(0).getTitle());
    }

    @Test
    void testSaveTask_PersistsToDatabase() {
        // Arrange
        Task task = Task.builder()
                .title("Test Task")
                .description("Test Description")
                .completed(false)
                .build();

        // Act
        Task savedTask = taskRepository.save(task);

        // Assert
        assertNotNull(savedTask.getId());
        assertEquals("Test Task", savedTask.getTitle());
        assertNotNull(savedTask.getCreatedAt()); // should be auto-generated
    }
}
