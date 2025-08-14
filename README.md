# HanYi.edu - Chinese Learning Platform (Java/Spring Boot) 🀄

Web application for Chinese courses

**Tech Stack**:  
Java 17 | Spring Boot 3 | PostgreSQL | Maven | Bootstrap | jQuery | Swagger | Docker

---
## 📚 API Endpoints

### Categories
| Method | Endpoint                      | Description                  |
|--------|-------------------------------|------------------------------|
| GET    | `/api/categories`             | Get all categories           |
| POST   | `/api/categories`             | Create a new category        |
| GET    | `/api/categories/{id}`        | Get a category by ID         |
| PUT    | `/api/categories/{id}`        | Update a category            |
| DELETE | `/api/categories/{id}`        | Delete a category            |

### Courses
| Method | Endpoint                      | Description                  |
|--------|-------------------------------|------------------------------|
| GET    | `/api/courses`                | Get all courses              |
| POST   | `/api/courses`                | Create a new course          |
| GET    | `/api/courses/{id}`           | Get a course by ID           |
| PUT    | `/api/courses/{id}`           | Update a course              |
| DELETE | `/api/courses/{id}`           | Delete a course              |
| GET    | `/api/courses/category/{categoryId}` | Get courses by category ID |

### Lessons
| Method | Endpoint                      | Description                  |
|--------|-------------------------------|------------------------------|
| GET    | `/api/lessons`                | Get all lessons              |
| POST   | `/api/lessons`                | Create a new lesson          |
| GET    | `/api/lessons/{id}`           | Get a lesson by ID           |
| PUT    | `/api/lessons/{id}`           | Update a lesson              |
| DELETE | `/api/lessons/{id}`           | Delete a lesson              |
| GET    | `/api/lessons/course/{courseId}` | Get lessons by course ID   |

### Entries
| Method | Endpoint                      | Description                  |
|--------|-------------------------------|------------------------------|
| GET    | `/api/entries`                | Get all entries              |
| POST   | `/api/entries`                | Create a new entry           |
| GET    | `/api/entries/{id}`           | Get an entry by ID           |
| DELETE | `/api/entries/{id}`           | Delete an entry              |
| GET    | `/api/entries/user/{userId}`  | Get entries by user ID       |
| GET    | `/api/entries/course/{courseId}` | Get entries by course ID   |

### Flashcards
| Method | Endpoint                      | Description                  |
|--------|-------------------------------|------------------------------|
| GET    | `/api/flashcards`             | Get all flashcards           |
| POST   | `/api/flashcards`             | Create a new flashcard       |
| GET    | `/api/flashcards/{id}`        | Get a flashcard by ID        |
| PUT    | `/api/flashcards/{id}`        | Update a flashcard           |
| DELETE | `/api/flashcards/{id}`        | Delete a flashcard           |
| GET    | `/api/flashcards/user/{userId}` | Get flashcards by user ID   |

### Users
| Method | Endpoint                      | Description                  |
|--------|-------------------------------|------------------------------|
| GET    | `/api/users`                  | Get all users                |
| POST   | `/api/users`                  | Create a new user            |
| GET    | `/api/users/{id}`             | Get a user by ID             |
| PUT    | `/api/users/{id}`             | Update a user                |
| DELETE | `/api/users/{id}`             | Delete a user                |

[MIT License](https://mit-license.org/) | Home page based on Victory Educational template. credits: @learning-zone

## Project Structure

```bash
han-yi-edu/
├── src/
│   ├── main/
│   │   ├── java/edu/Han.Yi/
│   │   │   ├── AI/                  # AI-related components
│   │   │   │   ├── controller/  
│   │   │   │   └── dto/       
│   │   │   ├── constants/           # Logging constants
│   │   │   ├── controller/
│   │   │   │   ├── api/             # REST API controllers
│   │   │   │   └── frontend/        # Frontend controllers
│   │   │   ├── dto/                 # Data Transfer Objects
│   │   │   ├── exception/           # Custom exceptions
│   │   │   ├── model/               # Entities
│   │   │   ├── repository/          # JPA repositories
│   │   │   ├── security/            # Auth and security
│   │   │   │   ├── config/          # Security configuration
│   │   │   │   ├── controller/      # Auth endpoints 
│   │   │   │   ├── request/  
│   │   │   │   ├── response/ 
│   │   │   │   └── service/ 
│   │   │   ├── service/             # Services and their implementations
│   │   │   ├── utils/               # Helpers
│   │   │   └── HanYiEduApplication.java
│   │   └── resources/
│   │       ├── db/migration/        # Flyway database migration scripts
│   │       ├── static/              # Static assets (CSS, JS, images, icons etc.)
│   │       ├── templates/           # Thymeleaf HTML templates
│   │       └── application.properties  # App config (DB, JWT, etc.)
│   └── test/                       # Tests
│       ├── controller/             # Controller layer tests
│       ├── entity/                 # Entity validation tests
│       ├── repository/             # Repository layer tests
│       ├── security/               # Security tests
│       └── service/                # Service layer tests
├── pom.xml               
├── Dockerfile     
└── docker-compose.yml 

