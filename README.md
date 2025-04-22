# sawal

Database interaction using natural language queries

This project provides a command-line application that allows users to interact with a MySQL database using natural language queries instead of writing SQL. It leverages the power of Google's Gemini large language model, integrated via Spring AI, to understand the user's intent and translate it into executable SQL queries.

The application works by first introspecting the target database ('library' by default) to understand its schema and retrieve some data samples. This structural and content information is then combined with the user's natural language question and sent as a detailed prompt to the Gemini API. The AI model, trained to generate SQL based on provided context, returns a suggested SQL query. The application then extracts this SQL, executes it against the database, and presents the results back to the user in a readable format for SELECT statements, or indicates the number of rows affected for update, insert, or delete operations.

The project is built using Spring Boot, providing a robust framework for managing components and configuration. It utilizes the Spring AI library for seamless integration with the Gemini API (specifically through the Vertex AI starter), JDBC and the MySQL Connector/J for database connectivity, and Google Auth for secure authentication with the Google Cloud services. The build process is managed by Gradle.

## Stack

| Technology         |
|--------------------|
| Gemini             |
| Spring AI (Vertex) |
| Java               |
| Gradle             |
| JDBC               |
| MySQL              |
| Google Auth Library|

## Try it out!
##### Prerequisites
```
Comprehensively tested on RHEL 9.5, YMMV with other OSes
openjdk-21
mysql
jdbc
gcloud
Google Cloud Project with a service account having Vertex AI + Gemini AI + Compute APIs accessible.
```
##### Commpile from source
1. `git clone https://github.com/Netherquark/sawal.git`
2. `cd sawal`
3. `mkdir src/main/resources`
4. `nano src/main/resources/application.properties`
5. add your `service-account-key.json` in /src/main/resources/
6. Compile: `./gradlew clean build --refresh-dependencies`
7. Run: `./gradlew bootRun --stacktrace`

##### Reference application.properties:
```
# MySQL Datasource
spring.datasource.url=jdbc:mysql://localhost:3306/library?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=johndoe
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# Spring AI Vertex Gemini configuration
spring.ai.vertex.ai.gemini.project-id=sawal-XXXXX
spring.ai.vertex.ai.gemini.location=us-central1
spring.ai.vertex.ai.gemini.credentials.location=src/main/resources/service-account-key.json
spring.ai.vertex.ai.gemini.chat.options.model=gemini-2.0-flash
spring.ai.vertex.ai.gemini.chat.options.temperature=0.2
```

## Project structure & class diagram:

```

com/
└── sawal/
├── Main.java
│   └── class Main
│       ├── main()                     // Application entry point
│       └── run()                      // CLI execution logic, delegates query processing to QueryOrchestrator
│
├── ai/
│   ├── GeminiAPIClient.java
│   │   └── class GeminiAPIClient
│   │       ├── sendQuery()              // Sends the prompt to Gemini and retrieves the response
│   │       ├── buildRequestBody()       // Formats request payload for Gemini API
│   │       ├── getAccessToken()         // Retrieves OAuth2 access token using service account credentials
│   │       └── parseResponse()          // Parses Gemini's response to extract the text content
│   │
│   └── SpringAIProcessor.java
│       └── class SpringAIProcessor
│           ├── processQuery()         // Builds and sends the enriched prompt to GeminiAPIClient
│           └── buildPrompt()          // Constructs the final AI prompt with system instructions and user input
│
├── db/
│   ├── DatabaseConnector.java
│   │   └── class DatabaseConnector
│   │       ├── connect()              // Establishes JDBC connection to MySQL
│   │       ├── getConnection()        // Provides access to the underlying JDBC Connection for metadata
│   │       ├── query()                // Executes a SQL query and returns a ResultSet
│   │       ├── update()               // Executes a SQL update, insert, or delete statement
│   │       ├── insert()               // Executes a SQL insert statement
│   │       ├── delete()               // Executes a SQL delete statement
│   │       ├── findById()             // Fetches a single row by ID
│   │       ├── update(String rawSql)  // Executes raw SQL statements (e.g., DDL)
│   │       ├── interpolate()          // Helper for interpolating SQL for logging
│   │       └── closeConnection()      // Closes the database connection
│   │
│   ├── QueryOrchestrator.java
│   │   └── class QueryOrchestrator
│   │       ├── handle()               // End-to-end processing: schema -> LLM -> SQL -> execution
│   │       ├── isSelect()             // Checks if a query is a SELECT statement
│   │       ├── extractSql()           // Strips Markdown-style formatting from AI-generated SQL
│   │       └── executeSelect()        // Executes a SELECT query and formats the output
│   │
│   └── SchemaGenerator.java
│       └── class SchemaGenerator
│           └── getSchema()            // Retrieves schema and data for the target database
│
└── exceptions/
├── APIException.java
│   └── class APIException           // Generic runtime exception for API-layer errors
│
├── DatabaseException.java
│   └── class DatabaseException      // Thrown for problems connecting to or querying the database
│
├── QueryException.java
│   └── class QueryException         // Base class for errors during SQL construction or execution
│
└── InvalidQueryException.java
└── class InvalidQueryException  // Thrown for syntactically invalid or disallowed user-supplied SQL
```
