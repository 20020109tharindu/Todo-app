# 📝 To-Do Task Web Application  

## 🚀 Overview  
This is a **Full Stack To-Do Task Management Application** built as part of the **Full Stack Engineer Take-Home Assessment**.  
The app allows users to:  
- ✅ Create to-do tasks by providing a **title** and **description**.  
- 🕔 View only the **most recent 5 tasks**.  
- 🧬 Mark tasks as **completed**, which automatically hides them from the list.  

All components — **frontend**, **backend**, and **database** — are **fully containerized** and can be run using **Docker Compose**.

---

## 🏧 Architecture  

The system consists of three main services:  

| Component | Technology | Description |
|------------|-------------|-------------|
| **Frontend** | React (Vite) | Single Page Application (SPA) for task management |
| **Backend** | Spring Boot (Java 17) | REST API for managing tasks |
| **Database** | MySQL 8.0 | Relational database storing tasks |
| **Testing** | JUnit, Mockito, MockMvc, H2 | Backend unit and integration testing |

---

## 🧉 System Design  

**Data Model (task table):**
| Column | Type | Description |
|---------|------|-------------|
| `id` | BIGINT (PK, Auto Increment) | Unique identifier for the task |
| `title` | VARCHAR(255) | Task title |
| `description` | VARCHAR(255) | Task description |
| `completed` | BOOLEAN | Task completion status |
| `created_at` | DATETIME | Auto-generated timestamp |

---

## 🐳 Docker Setup  

### 1️⃣ Prerequisites  
Ensure you have the following installed:
- Docker  
- Docker Compose  
- Git  

### 2️⃣ Clone the Repository  
```bash
git clone https://github.com/<your-username>/todo-app.git
cd todo-app
```

### 3️⃣ Run All Containers  
```bash
docker-compose up --build
```

This will start:  
- 🗄️ `todo-db` → MySQL database on port **3307**  
- ⚙️ `todo-backend` → Spring Boot backend on port **8080**  
- 💻 `todo-frontend` → React frontend on port **80**

### 4️⃣ Access the Application  
Open your browser and navigate to:  
🔗 **http://localhost**

---

## 🧪 Running Tests  

### 🧠 Backend Tests
Run inside the backend directory:
```bash
mvn test
```

This executes:
- **Unit tests** (Mockito-based) for service and repository layers  
- **Integration tests** (MockMvc-based) for REST API endpoints  
- Uses **H2 in-memory database** for isolation  

🗾 A coverage report can be generated using:
```bash
mvn clean verify
```
You can then find coverage reports under:
```
backend/target/site/jacoco/index.html
```

---

## ⚙️ Configuration  

### 🗂️ `src/main/resources/application.properties`
```properties
spring.datasource.url=jdbc:mysql://database:3306/todo_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=rootpassword
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
```

### 🧪 `src/test/resources/application-test.properties`
```properties
spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
```

---

## 🧱 Project Structure  

```
todo-app/
├── backend/
│   ├── src/
│   │   ├── main/java/com/example/todo_backend/
│   │   ├── test/java/com/example/todo_backend/
│   │   └── resources/
│   ├── pom.xml
│   └── Dockerfile
│
├── frontend/
│   ├── src/
│   ├── package.json
│   └── Dockerfile
│
├── docker-compose.yml
└── README.md
```

---

## 🤓 API Endpoints  

| Method | Endpoint | Description |
|---------|-----------|-------------|
| `GET` | `/api/tasks` | Fetch recent 5 incomplete tasks |
| `POST` | `/api/tasks` | Create a new task |
| `PATCH` | `/api/tasks/{id}/complete` | Mark a task as completed |

---

## 🧠 Tests Implemented  

### ✅ **Unit Tests**
- `TaskServiceImplTest.java` → Service layer tests using Mockito  
- `TaskRepositoryTest.java` → Repository layer tests with H2  

### ✅ **Integration Tests**
- `TaskControllerIntegrationTest.java` → Full REST API testing using MockMvc and H2  

---

## 🤉 Notes  
- H2 Database is **only used for testing** (isolated from MySQL).  
- All tests run automatically during `mvn test`.  
- MySQL volume ensures persistent task data even after container restart.  

---

## 🏁 Author  
👨‍💻 **H.P.A.T. Lakshan**  
BSc (Hons) in Information Technology (Specialized in IT)  
Email: *[Add your email here]*  
