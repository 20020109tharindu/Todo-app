package com.example.todo_backend.controller;

import com.example.todo_backend.model.Task;
import com.example.todo_backend.repository.TaskRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest // Loads full Spring Boot context for integration testing
@AutoConfigureMockMvc // Enables MockMvc for controller testing
@Transactional // Rolls back DB after each test
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test") // Uses application-test.properties (H2)
class TaskControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        // Reset DB before each test to maintain isolation
        taskRepository.deleteAll();
    }

    @Test
    void testGetRecentTasks_ReturnsEmptyList_WhenNoTasks() throws Exception {
        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void testCreateTask_ReturnsCreatedTask() throws Exception {
        Task task = Task.builder()
                .title("New Task")
                .description("New Description")
                .completed(false)
                .build();

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("New Task"))
                .andExpect(jsonPath("$.description").value("New Description"))
                .andExpect(jsonPath("$.completed").value(false))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.createdAt").exists());
    }

    @Test
    void testGetRecentTasks_ReturnsAllTasks() throws Exception {
        for (int i = 1; i <= 3; i++) {
            Task task = Task.builder()
                    .title("Task " + i)
                    .description("Description " + i)
                    .completed(false)
                    .build();
            taskRepository.save(task);
        }

        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].title").value("Task 3"))
                .andExpect(jsonPath("$[2].title").value("Task 1"));
    }

    @Test
    void testMarkTaskAsDone_CompletesTask() throws Exception {
        Task task = Task.builder()
                .title("Task to Complete")
                .description("Description")
                .completed(false)
                .build();
        Task savedTask = taskRepository.save(task);

        mockMvc.perform(patch("/api/tasks/" + savedTask.getId() + "/complete"))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void testMarkTaskAsDone_Returns404_WhenTaskNotFound() throws Exception {
        mockMvc.perform(patch("/api/tasks/999/complete"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetRecentTasks_ReturnsMaximum5Tasks() throws Exception {
        for (int i = 1; i <= 7; i++) {
            Task task = Task.builder()
                    .title("Task " + i)
                    .description("Description " + i)
                    .completed(false)
                    .build();
            taskRepository.save(task);
        }

        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(5)))
                .andExpect(jsonPath("$[0].title").value("Task 7"))
                .andExpect(jsonPath("$[4].title").value("Task 3"));
    }
}
