package com.sawal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.sawal.ai.SpringAIProcessor;

@SpringBootApplication
public class Main implements CommandLineRunner {

    @Autowired
    private SpringAIProcessor aiProcessor;

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // The natural language query
        String naturalLanguageQuery = "What is the 3rd book hawking published";
        // Process the query and get a SQL string (or response from Gemini)
        String sqlQuery = aiProcessor.processQuery(naturalLanguageQuery);
        System.out.println("Generated SQL Query: " + sqlQuery);
    }
}
