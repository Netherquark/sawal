package com.sawal;

import com.sawal.db.QueryOrchestrator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main implements CommandLineRunner {

    private final QueryOrchestrator orchestrator;

    @Autowired
    public Main(QueryOrchestrator orchestrator) {
        this.orchestrator = orchestrator;
    }

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Main.class);
        app.setWebApplicationType(WebApplicationType.NONE); // CLI mode
        app.run(args);
    }

    @Override
    public void run(String... args) {
        // Read the user's natural‑language query or fall back to a default
        String query = args.length > 0
                     ? String.join(" ", args)
                     : "What is the 3rd book Hawking published";

        // Delegate end‑to‑end processing (schema → LLM → SQL → execution)
        String output = orchestrator.handle(query);

        // Print the combined result
        System.out.println(output);
    }
}
