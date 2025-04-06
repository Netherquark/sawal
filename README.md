# sawal

Database interaction using natural language queries

## Project structure & class diagram:

```
com/
└── nldbquery/
    ├── Main.java
    │   └── class Main
    │         ├── main()                        // Application entry point
    │         ├── captureUserQuery()           // Captures natural language query input from the user
    │         ├── processQuery()               // Processes the query using Gemini API and Spring AI
    │         └── executeDatabaseQuery()       // Executes the generated SQL query via JDBC
    │
    ├── ai/
    │   ├── GeminiAPIClient.java
    │   │   └── class GeminiAPIClient
    │   │         ├── sendQuery()              // Sends the natural language query to Gemini API and retrieves a response
    │   │         └── parseResponse()          // Parses the response to convert natural language into an SQL query
    │   │
    │   └── SpringAIProcessor.java
    │         └── class SpringAIProcessor
    │               ├── enhanceQuery()         // Enhances query interpretation using Spring AI
    │               └── validateSQL()          // Validates the generated SQL syntax
    │
    ├── db/
    │   └── DatabaseConnector.java
    │         └── class DatabaseConnector
    │               ├── connect()              // Establishes a JDBC connection to the MySQL database
    │               ├── executeSQL()           // Executes the SQL query on the database
    │               └── closeConnection()      // Closes the database connection
    │
    ├── utils/
    │   └── QueryFormatter.java
    │         └── class QueryFormatter
    │               └── formatQuery()          // Formats the natural language query into a standard SQL format
    │
    └── exceptions/
          ├── NLDBQueryException.java
          │   └── class NLDBQueryException      // Base custom exception for NLDBQuery application
          │
          ├── APIException.java
          │   └── class APIException            // Thrown for Gemini or Spring AI errors
          │
          ├── DatabaseException.java
          │   └── class DatabaseException       // Thrown for database-related failures
          │
          └── InvalidQueryException.java
              └── class InvalidQueryException   // Thrown for invalid or malformed queries
```
