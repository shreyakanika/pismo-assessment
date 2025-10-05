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

## Endpoints

### Add account
- The request body should receive a document number that uniquely identifies the account owner.
- The response body should return the account object if successful, or an error message otherwise.
```
Request:
    POST /accounts
    {
        "document_number": "12345678900"
    }
```
```
Response:
    {
        "account_id": 1,
        "document_number": "12345678900"
    }
```

### Get account
- The endpoint should receive the accountId as a query parameter.
- An invalid accountId value should return an error message.
```
Request:
GET /accounts/:accountId
```
```
Response:
    {
        "account_id": 1,
        "document_number": "12345678900"
    }
```

### Create transactions
- The request body should receive the account owner ID, the operation type and the amount for the transaction;
- The system should validate whether the existing account owner and operation type were informed.
- The response body should return the transaction object if successful, or an error message otherwise.
```
Request:
    POST /transactions
    {
        "account_id": 1,
        "operation_type_id": 4,
        "amount": 123.45
    }
```
```
Response:
    {
        "transaction_id": 1,
        "account_id": 1,
        "operation_type_id": 4,
        "event_date": "2025-09-30T09:33:51.073059",
        "amount": 123.45
    }
```


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

