# sawal
 


 

Database interaction using natural language queries
 


 

Stack:
 

* Gemini
 

* Spring AI (Vertex)
 

* Java
 

* Gradle
 

* JDBC
 

* MySQL
 


 

## Project structure & class diagram:
 


 

```
com/
└── sawal/
    ├── Main.java
    │   └── class Main
    │         ├── main()                        // Application entry point
    │         ├── run()                         // CLI execution logic for query processing
    │         └── processQuery()                // Orchestrates AI processing and outputs the result
    │
    ├── ai/
    │   ├── GeminiAPIClient.java
    │   │   └── class GeminiAPIClient
    │   │         ├── sendQuery()              // Sends the prompt to Gemini and retrieves the response
    │   │         ├── buildRequestBody()       // Formats request payload for Gemini API
    │   │         ├── getAccessToken()         // Retrieves OAuth2 access token using service account credentials
    │   │         └── parseResponse()          // Parses Gemini's response to extract SQL
    │   │
    │   └── SpringAIProcessor.java
    │         └── class SpringAIProcessor
    │               ├── processQuery()         // Prepares and sends structured prompts using Spring AI
    │               └── buildPrompt()          // Constructs the final AI prompt with system instructions and user input
    │
    ├── db/
    │   └── DatabaseConnector.java
    │         └── class DatabaseConnector
    │               ├── connect()              // Establishes JDBC connection to MySQL
    │               ├── executeSQL()           // Executes a provided SQL query and returns results
    │               └── closeConnection()      // Closes the database connection cleanly
    │
    ├── utils/
    │   └── QueryFormatter.java
    │         └── class QueryFormatter
    │               └── cleanSQL()             // Strips Markdown-style formatting from AI-generated SQL
    │
    └── exceptions/
          ├── NLDBQueryException.java
          │   └── class NLDBQueryException      // Base exception for natural language to DB errors
          │
          ├── APIException.java
          │   └── class APIException            // Thrown for failures in API communication or response parsing
          │
          ├── DatabaseException.java
          │   └── class DatabaseException       // Thrown for SQL execution or DB connection errors
          │
          └── InvalidQueryException.java
              └── class InvalidQueryException   // Thrown for ill-formed or incomplete natural language queries
```
