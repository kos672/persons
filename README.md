# Person data processing application

### API documentation available at
* http://localhost:8080/swagger-ui/index.html

### Brief summary of the API
| Method | Path                  | Description                                                                   |
|--------|-----------------------|-------------------------------------------------------------------------------|
| POST   | /api/persons          | Create a person, create a task and submit it to process fields asynchronously |
| GET    | /api/tasks            | Retrieve all tasks                                                            |
| GET    | /api/tasks/{taskId}   | Retrieve a task for the given taskId                                          |


### Start the application via Gradle

Start the application (with standard task processing delay as 3000ms)
><code>./gradlew bootRun</code>

Start the application (with custom task processing delay, e.g. 1500ms)
><code>./gradlew bootRun --args='--persons.processing.slowdown.time.ms=1500'</code>

### Docker compose

Start the application via docker compose (with standard task processing delay as 3000ms)
><code>./gradlew bootJar && docker compose up</code>

Start the application via docker compose (with custom task processing delay, e.g. 1500ms)
><code>./gradlew bootJar && PERSONS_PROCESSING_SLOWDOWN_TIME_MS=1500 docker compose up</code>