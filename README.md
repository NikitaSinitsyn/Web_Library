# Employee Management System

The Employee Management System is a Java-based web application developed using Spring Boot and PostgreSQL. This project provides a complete solution for managing employee data, enabling businesses to efficiently handle employee information, positions, salaries, and generate reports. It serves as a backend for an employee management application, allowing users to perform CRUD (Create, Read, Update, Delete) operations on employee data.

---

### Key Features:

Employee Data Management: The system allows users to add, view, update, and delete employee records. Each employee record contains essential information such as name, salary, and position.

Position Management: The application also facilitates the management of positions within the organization. Users can create and modify positions, which can be assigned to employees.

Salary Management: The system tracks and manages salary information for each employee, enabling easy salary updates and access to employees with the highest salary.

Employee Reports: Users can generate and retrieve employee reports, providing detailed insights into employee data. The reports can be downloaded for further analysis.

Pagination: The application incorporates pagination for the employee list, ensuring efficient handling of large datasets.

---

### Testing and Validation:

The project includes comprehensive unit tests for the service layer and integration tests to verify database connectivity using Testcontainers. These tests help ensure the correctness and reliability of the implemented functionalities.

---

### Technology Stack:

- Java
- Spring Boot
- PostgreSQL
- Mockito
- Testcontainers
- JUnit
- Maven

---

### How to Run:

To run the Employee Management System locally, follow these steps:

1. Ensure that you have Java and Maven installed on your system.
2. Set up the PostgreSQL database either locally or using Docker with the provided docker-compose.yml.
3. Update the application.properties file with the appropriate database configuration.
4. Build the application using Maven.
5. Run the application, and it will be accessible on the specified port.

---

### Contributing:

We welcome contributions to enhance the functionality, improve test coverage, or fix any issues in the project. To contribute, fork the repository, create a new branch, and submit a pull request. All contributions will be reviewed and merged accordingly.
