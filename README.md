# UPT Web Application

## Overview

The **UPT Web Application** is a comprehensive academic management system designed to facilitate the administration of various academic activities at **Universidade Portucalense Infante D. Henrique (UPT)**. The application is built using modern web technologies and frameworks to provide a robust, scalable, and user-friendly experience.

## Features

- **User Authentication and Session Management**: Secure login and session management for different user roles (Master, Director, Coordinator).
- **Curricular Unit Management**: Create, edit, and manage curricular units.
- **Room Management**: Add, edit, and remove rooms.
- **PDF Generation**: Generate PDFs for various academic reports.
- **Role-Based Access Control**: Different functionalities and access levels for Masters, Directors, and Coordinators.
- **Responsive Design**: User-friendly interface optimized for various devices.

## Technologies Used

### Backend

- **Java 17**: Core programming language for the backend.
- **Spring Boot 3.3.5**: Framework for building backend services.
- **Spring Boot Starter Data JPA**: For database interactions.
- **Spring Boot Starter Web**: For building web applications.
- **Spring Boot Starter Thymeleaf**: For server-side HTML rendering.
- **Spring Boot Starter Logging**: For logging purposes.
- **Hibernate 6.5.3.Final**: ORM framework for database operations.
- **Jakarta Validation API 3.0.2**: For input validation.
- **HikariCP**: Connection pooling for efficient database access.
- **MySQL**: Primary database used for storing application data.
- **H2 Database**: In-memory database for testing.
- **iTextPDF 5.5.13.2**: For generating PDF documents.

### Frontend

- **Thymeleaf**: Template engine for rendering HTML on the server side.
- **HTML5 & CSS3**: For structuring and styling web pages.
- **JavaScript**: For client-side scripting and interactivity.

### Build and Dependency Management

- **Gradle**: Build automation tool used for managing dependencies and building the project.

### Testing

- **JUnit 5**: For unit and integration testing.
- **Mockito**: For mocking dependencies in tests.
- **Spring Boot Test**: For testing Spring Boot applications.

## Project Structure

```
├── .gitattributes
├── .gitignore
├── .gradle/
├── .vscode/
├── bin/
├── build/
├── gradle/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/upt/upt/
│   │   │       ├── controller/
│   │   │       ├── entity/
│   │   │       ├── repository/
│   │   │       ├── service/
│   │   │       ├── UptWebApplication.java
│   │   │       ├── UptWebConfig.java
│   │   │       └── UptWebController.java
│   │   ├── resources/
│   │       ├── static/
│   │       ├── templates/
│   │       └── application.properties
│   ├── test/
│       ├── java/
│       │   └── com/upt/upt/
│       │       ├── UptWebApplicationTest.java
│       │       └── UptWebControllerTest.java
│       └── resources/
│           └── application-test.properties
├── build.gradle
├── gradlew
├── gradlew.bat
├── HELP.md
└── settings.gradle
```

## Getting Started

### Prerequisites

Before getting started, ensure the following are installed:

- **Java 17**
- **MySQL**: Set up a MySQL database.
- **Gradle**: Ensure Gradle is installed.

### Configuration

1. **Database Configuration**: Update the `application.properties` file with your MySQL database credentials:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/uptQsProjectDB
   spring.datasource.username=root
   spring.datasource.password=password
   ```

### Building the Project

To build the project, navigate to the project root directory and run:

```bash
./gradlew build
```

### Running the Application

To run the application, use the following command:

```bash
./gradlew bootRun
```

The application will start on port **8080** by default. You can access it at [http://localhost:8080](http://localhost:8080).

### Running Tests

To run the tests, use the following command:

```bash
./gradlew test
```

## Usage

### Accessing the Application

- **Login**: Navigate to [http://localhost:8080/login](http://localhost:8080/login) to access the login page.
- **Roles**: Depending on the user role (Master, Director, Coordinator), you will be redirected to different dashboards after logging in.

### Managing Curricular Units

- **Create UC**: Navigate to the **Coordinator** dashboard and click on **"Create UC"**.
- **Edit UC**: Select a UC from the list and click on **"Edit"**.

### Managing Rooms

- **Add Room**: Navigate to the **Master** dashboard and click on **"Add Room"**.
- **Edit Room**: Select a room from the list and click on **"Edit"**.

### Generating PDFs

- **Generate PDF**: Navigate to the relevant section (e.g., **Coordinator** dashboard) and click on **"Generate PDF"**.

## Additional Information

For further reference, please check out the following resources:

- [Official Gradle Documentation](https://docs.gradle.org)
- [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/current/gradle-plugin/reference/html/)
- [Create an OCI Image](https://docs.gradle.org/current/userguide/gradle_docker_plugin.html)
- [Gradle Build Scans](https://scans.gradle.com)

## License

This project is licensed under the **Apache License, Version 2.0**. See the LICENSE file for details.

---

Thank you for using the UPT Web Application. If you have any questions or need further assistance, feel free to contact us!
