# Backend development

The backend is built using Spring Boot with Maven as the build tool. The project structure is organized as follows:
- `src/main/java`: Contains the main application code
- `src/main/resources`: Contains configuration files and static resources
- `src/test/java`: Contains test cases for the application
- `pom.xml`: Maven configuration file
- `application.properties`: Application configuration file
- `Dockerfile`: Docker configuration for containerizing the application

## Development Guidelines

### Getting started

#### IntelliJ

1. Open the project from the PROJECT ROOT directory.
2. Ensure that Maven is configured correctly in IntelliJ.
   - For sanity check, right-click on the `pom.xml` file and select `Maven > Sync Project` to ensure all dependencies and plugins are present.
3. Create `.env` file to project root based off `.env.example` and fill in the required environment variables.
4. In order to get the environment variables correctly working in dev, go to IntelliJ's `Run -> Edit Configurations` (top left 3 lines hamburger) and do the following:
   - Choose a convenient name for your configuration, e.g., `OnlineFlashcards API` (doesn't matter)
   - Select the `Application` configuration for the backend
   - Choose SDK version 17 or higher
   - Set the main class to `com.example.flashcards.OnlineFlashcardsApi`
   - In the `Environment variables` field, click on the folder icon and select the `.env` file you created in step 3.
5. Run the application using the IntelliJ run button.
6. If everything went correctly, the last thing printed in terminal should be something like:
    ```plaintext
    ... Started OnlineFlashcardsAPi in XX seconds
    ```
   
TODO: instructions if db fails

### Backend structure

The backend follows a layered architecture pattern, separating concerns into different layers:

- **auth/**: Contains authentication and authorization logic.
- **common/**: Contains common utilities, constants, and configurations used across the application.
- **config/**: Contains configuration classes for the application.
- **security/**: Contains security-related classes and configurations.

### Creating endpoints/resources

When creating new endpoints, follow this structure:

```
com.example.flashcards.entity/
 ├── <resource_name>/
 │    ├── <resource_name>               # the core domain model
 │    ├── <resource_name>Controller     # main entrypoint for HTTP requests
 │    ├── <resource_name>IService       # interface for the service
 │    ├── <resource_name>Repository     # extends JpaRepository, interacts with the database
 │    ├── <resource_name>Service        # implementation of the service interface (interacts with repository)
 │    ├── dto/                          # data transfer objects (Records)
 │    │    ├── <ResourceName>Response
 │    │    ├── ... other necessary dtos
 │    ├── ... any necessary Enums etc.
```

An example of each file can be found in the `com.example.flashcards.user` package.

#### DTOs

Use Java Records for DTOs. Place them in the `dto/` subpackage. Name them according to their purpose, e.g., `<ResourceName>Response`, `<ResourceName>CreateRequest`, etc.
JPA annotations work well in validating e.g. incoming requests etc. so use them in the DTOs when appropriate.

#### Exceptions

Use the generalized `ApiException` class for throwing exceptions. Create specific exception classes extending `ApiException` if needed.

Common exceptions are defined in `common/exception/` package.

#### Services

Define service interfaces in the `IService` class and implement them in the `Service` class. Services should contain business logic and interact with repositories.

#### Repositories

Repositories should extend `JpaRepository` and be placed in the `Repository` class. They handle database interactions.

#### Controllers

Controllers handle direct HTTP requests. They should call the service layer to perform operations and return appropriate responses.

### Testing

Write unit tests for services and controllers in the `src/test/java` directory. Use JUnit and Mockito for testing. Ensure that all new functionality is covered by tests.

Classes of an entity that REQUIRE tests are the Service and Controller.
