package com.sawal.ai;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SpringAIProcessor {

    private static final Logger logger = LoggerFactory.getLogger(SpringAIProcessor.class);
    private final GeminiAPIClient geminiAPIClient;

    private static final String SYSTEM_PROMPT = """
        You are a helpful assistant that translates natural language questions into optimized SQL queries for a MySQL database.
        Only respond with a valid SQL query.
        Wrap the query in triple backticks using the 'sql' language identifier.
        Do not include any explanation.
        If the query cannot be generated, respond with: "Insufficient data to generate SQL."
        """;

    public SpringAIProcessor(GeminiAPIClient geminiAPIClient) {
        this.geminiAPIClient = geminiAPIClient;
    }

    /**
     * Processes a natural language request and returns the generated SQL query.
     *
     * @param naturalLanguageQuery the plain English input
     * @return the resulting SQL query
     */
    public String processQuery(String naturalLanguageQuery) {
        String prompt = buildPrompt(naturalLanguageQuery);
        logger.info("Sending prompt to Gemini: {}", prompt);
        return geminiAPIClient.sendQuery(prompt);
    }

    /**
     * Builds a clean prompt combining the system instructions and user input.
     */
    private String buildPrompt(String userInput) {
        return String.format("""
            %s

            Convert the following request into a SQL query:
            "%s"
            """, SYSTEM_PROMPT, userInput);
    }
}
