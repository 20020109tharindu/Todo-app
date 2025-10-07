package com.example.todo_backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test") // <-- uses H2
class TodoBackendApplicationTests {

	@Test
	void contextLoads() {
	}

}
