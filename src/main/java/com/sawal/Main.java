package com.sawal;

import com.sawal.ai.SpringAIProcessor;
import com.sawal.db.SchemaGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main implements CommandLineRunner {

    private final SpringAIProcessor aiProcessor;
    private final SchemaGenerator schemaGenerator;

    @Autowired
    public Main(SpringAIProcessor aiProcessor, SchemaGenerator schemaGenerator) {
        this.aiProcessor = aiProcessor;
        this.schemaGenerator = schemaGenerator;
    }

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Main.class);
        app.setWebApplicationType(WebApplicationType.NONE); // CLI mode
        app.run(args);
    }

    @Override
    public void run(String... args) {
        // Original user query or default
        String input = args.length > 0
                ? String.join(" ", args)
                : "What is the 3rd book Hawking published";

        System.out.println("\nProcessing query: " + input);

        // 1) Fetch the current DB schema
        String schema = schemaGenerator.getSchema();
        System.out.println("Using database schema:\n" + schema);

        // 2) Prepend schema to the user query as context
        String enrichedInput = "Database schema:\n"
                + schema
                + "\n\n"
                + input;

        // 3) Send enriched prompt to Gemini
        String result = aiProcessor.processQuery(enrichedInput);

        // 4) Print out the LLM’s SQL response
        System.out.println("Gemini Response:\n" + result);
    }
}