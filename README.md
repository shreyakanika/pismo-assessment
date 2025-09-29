# Pismo Code Assessment

This project implements the initial requirements for the Pismo Code Assessment using Spring Boot 3, Spring Data JPA, and
H2 in-memory database.

## Prerequisites

- Java 17+
- Maven
- Docker (for containerization)

## How to Run Locally

1) Build the application JAR:
   `mvn clean package`

2) Run the application:
   `java -jar target/pismo-assessment-0.0.1-SNAPSHOT.jar`

The application will be accessible at http://localhost:8080.

## How to Run using Docker

- Build the application JAR (if not already done):
  `mvn clean package`

- Build the Docker image:
  `docker build -t pismo-service .`

- Run the Docker container:
  `docker run -p 8080:8080 pismo-service`

The application will be accessible at http://localhost:8080.

## API Documentation (OpenAPI / Swagger)

The API is documented using SpringDoc (OpenAPI 3).

- Swagger UI URL (In-Browser): http://localhost:8080/swagger-ui.html

- OpenAPI JSON Spec: http://localhost:8080/v3/api-docs

## Initial Data

The OperationType lookup table is automatically populated on startup with the required values:

| ID | Description          | Sign     |
|----|----------------------|----------|
| 1  | PURCHASE             | Negative |
| 2  | INSTALLMENT PURCHASE | Negative |
| 3  | WITHDRAWAL           | Negative |
| 4  | PAYMENT              | Positive |

