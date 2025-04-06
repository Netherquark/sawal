package com.sawal;

import com.sawal.ai.SpringAIProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main implements CommandLineRunner {

    private final SpringAIProcessor aiProcessor;

    @Autowired
    public Main(SpringAIProcessor aiProcessor) {
        this.aiProcessor = aiProcessor;
    }

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Main.class);
        app.setWebApplicationType(org.springframework.boot.WebApplicationType.NONE); // CLI mode
        app.run(args);
    }

    @Override
    public void run(String... args) {
        String input = args.length > 0
                ? String.join(" ", args)
                : "What is the 3rd book Hawking published";

        System.out.println("\nProcessing query: " + input);

        String result = aiProcessor.processQuery(input);

        System.out.println("Gemini Response:\n" + result);
    }
}
