# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a Spring Boot 3.5.5 application using Java 17 and Maven. It's a basic web server project with the group ID `com.blog` and artifact ID `maven-server`. The project follows standard Spring Boot conventions with DevTools enabled for development.

## Build and Development Commands

### Building the project
```bash
./mvnw clean compile
```

### Running the application
```bash
./mvnw spring-boot:run
```

### Running tests
```bash
./mvnw test
```

### Running a single test
```bash
./mvnw test -Dtest=MavenServerApplicationTests
```

### Building JAR
```bash
./mvnw clean package
```

### Running built JAR
```bash
java -jar target/maven-server-0.0.1-SNAPSHOT.jar
```

## Architecture

- **Main Application**: `src/main/java/com/blog/mavenserver/MavenServerApplication.java` - Standard Spring Boot main class
- **Package Structure**: `com.blog.mavenserver` - Main package for all application classes
- **Configuration**: `src/main/resources/application.properties` - Contains application name configuration
- **Tests**: Located in `src/test/java/com/blog/mavenserver/` following Maven conventions

## Dependencies

- **Spring Boot Starter Web**: For REST API and web functionality
- **Spring Boot DevTools**: For development-time features (hot reload, etc.)
- **Spring Boot Starter Test**: JUnit 5 testing framework with Spring Boot test utilities
- **Spring Boot Starter Data JPA**: For database operations and ORM
- **MySQL Connector**: MySQL database driver

## Development Notes

- Uses Maven wrapper (`./mvnw`) - no need to install Maven globally
- Java 17 is required
- DevTools is included for automatic restart during development
- The application runs on default Spring Boot port (8080) unless configured otherwise

## Database Configuration

- **Database**: MySQL 8.0
- **Database Name**: server_blog
- **Connection**: localhost:3306
- **Username**: root
- **Password**: 123456
- **JPA**: Hibernate with manual DDL management (no automatic schema updates)
- **SQL Logging**: Enabled for development
- **Database Management**: All database schema changes must be done manually
- **Startup Logging**: Configured for clean output with essential connection information
- **Table Creation**: All tables must use utf8mb4 charset with utf8mb4_unicode_ci collation for proper Chinese character support

## API Development Rules

1. **All API endpoints must use POST requests** - Use `@PostMapping` for all controller methods
2. **Create API.md documentation** - When first API endpoint is created, generate an 'API.md' file containing:
   - Interface summary table at the beginning with clickable links to detailed sections
   - Endpoint URLs with request/response examples 
   - Each interface section ends with a "Back to Top" link that jumps to the summary table
3. **Update API.md after changes** - When adding new interfaces or modifying existing ones, immediately update the API.md file with current endpoint information and summary table
4. **Startup logging requirements** - Always ensure these key information are logged:
   - Database connection status and URL
   - Server startup port and accessible URLs
   - Keep other startup logs minimal and clean
5. **Use Chinese for all backend log messages** - All business logic log messages (login success, user operations, error messages, etc.) must be in Chinese for better readability