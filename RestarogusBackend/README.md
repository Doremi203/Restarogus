# RestarogusBackend

RestarogusBackend is a restaurant management system backend developed using Docker, Kotlin, PostgreSQL, Spring Boot, and Gradle.

## Features

- **User Authentication**: Register and login functionality for users.
### Admin
- **Menu Management**: Add, update, and remove menu items. The system also keeps track of the quantity of each item.
- **Restaurant Statistics**: Provides various statistics about the restaurant such as revenue, loss, most popular dish, average rating, and order count over a specific period.
### Customer
- **Order Management**: Place, update, and cancel orders. The system also provides real-time updates on the status of orders.

## Project Structure

The project is structured into various packages:

- `org.amogus.restarogus.config`: Contains application configuration classes.
- `org.amogus.restarogus.controllers`: Contains the controllers for handling HTTP requests.
- `org.amogus.restarogus.exceptions`: Contains the custom exceptions.
- `org.amogus.restarogus.services`: Contains the business logic of the application.
- `org.amogus.restarogus.filters`: Contains spring filters.
- `org.amogus.restarogus.repositories`: Contains the data access layer of the application.
- `org.amogus.restarogus.models`: Contains the data models used in the application.
- `org.amogus.restarogus.requests`: Contains the request models for the API.
- `org.amogus.restarogus.responses`: Contains the response models for the API.
- `org.amogus.restarogus.validators`: Contains validators for services.

## Patterns
Application uses the following patterns:
### Spring
- **Builder**: Used by spring to build complex objects. 
For example, `ResponseEntity` is built using `ResponseEntityBuilder`. 
Builder also used in spring security to configure endpoints.
- **Chain of Responsibility**: Used by spring to handle requests. 
For example for filters.
- **Singleton**: Used by spring to manage beans.
### My Patterns
- **Observer**: Used to notify classes responsible for order preparation about order status changes.
`OrderPublisher` and  `OrderSubscriber` are used to implement this pattern.
- **Strategy**: Used to implement different strategies for priority queue for order preparation.
`OrderPriorityStrategy` is used to implement this pattern. By default, `FIFO` strategy is used.

## Setup

To set up the project, you need to have Kotlin, Java, Spring Boot, Gradle and Docker installed on your system.

1. Clone the repository.
2. Open the project in IntelliJ IDEA 2023.3.4 or any other IDE that supports Kotlin.
3. Build the project using Gradle.
4. Run the docker-compose file to start the database.
5. Run the application.

## Usage

Use with provided Postman collection.

