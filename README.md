
# Lototron Backend

A Spring Boot application for managing social lunch events.

## Quick Start

### Prerequisites

*   JDK 21
*   PostgreSQL

### Database Setup

1.  Create a PostgreSQL database (e.g., `lototron`).
2.  Update the database configuration in `src/main/resources/application.properties`.
3.  Navigate to the `database` folder in your project.
4.  Run the SQL scripts in the following order:
    *   `1_reset_database.sql`
    *   `2_create_tables.sql`
    *   `3_import.sql`

### Configuration

Update `src/main/resources/application.properties` if needed:

```properties
spring.datasource.url=jdbc:postgresql://localhost/lototron
spring.datasource.username=postgres
spring.datasource.password=student123
```
*(Note: It's generally recommended to use environment variables or external configuration for production secrets instead of hardcoding in `application.properties`.)*

### Build & Run Application

#### Using Gradle

```bash
# Build the application
./gradlew build

# Run the application
./gradlew bootRun
```

#### Using Maven

```bash
# Build the application
./mvnw clean install

# Run the application
./mvnw spring-boot:run
```

The application will start on `http://localhost:8080`.

## Testing the API

*   Access **Swagger UI** for interactive API documentation: `http://localhost:8080/swagger-ui.html`
*   Use tools like Postman or curl to interact directly with endpoints.

## Features

*   User management (login, register)
*   Lunch event planning and participation
*   Calendar view of workdays and events
*   In-app messaging system
*   Restaurant listings

## API Endpoints

*   `/login` - Authentication
*   `/register` - Create new user account
*   `/lunch/event` - Manage lunch events (create, view, join, etc.)
*   `/profile` - User profile operations (view, update)
*   `/calendar` - View workday calendar data
*   `/restaurant` - Access restaurant information
*   `/messages` - Interact with the messaging system

## Tech Stack

*   Java 21
*   Spring Boot 3.2.2
*   PostgreSQL
*   Hibernate/JPA
*   Lombok & MapStruct

## Contributors

*   Jaanus Tõnisson
*   Kätlin Koemets
*   Stella Timmer

## License

MIT License
